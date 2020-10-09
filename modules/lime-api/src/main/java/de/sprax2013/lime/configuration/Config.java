package de.sprax2013.lime.configuration;

import de.sprax2013.lime.LimeDevUtility;
import de.sprax2013.lime.configuration.validation.EntryValidator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class Config {
    private static final String ERR_NO_FILE = "You have to set a file for the configuration";

    private static Yaml yaml;

    private final Map<@NotNull String, @NotNull ConfigEntry> entries = new LinkedHashMap<>(),
            commentEntries = new HashMap<>();

    private final List<ConfigListener> cfgListeners = new ArrayList<>();

    private @Nullable File file;
    private @Nullable ConfigCommentProvider commentProvider;

    public Config() {
        this(null);
    }

    public Config(@Nullable File file) {
        this(file, (ConfigCommentProvider) null);
    }

    public Config(@Nullable File file, @Nullable String comment) {
        this(file, () -> comment);
    }

    public Config(@Nullable File file, @Nullable ConfigCommentProvider commentProvider) {
        this.file = Objects.requireNonNull(file);
        this.commentProvider = commentProvider;
    }

    public @Nullable File getFile() {
        return this.file;
    }

    public void setFile(@Nullable File file) {
        this.file = file;
    }

    public void setCommentProvider(@Nullable ConfigCommentProvider commentProvider) {
        this.commentProvider = commentProvider;
    }

    public @Nullable ConfigCommentProvider getCommentProvider() {
        return this.commentProvider;
    }

    public @Nullable String getFormattedComment() {
        String comment = this.commentProvider != null ? this.commentProvider.getComment() : null;

        if (comment != null && !comment.isEmpty()) {
            return "# " + comment.replace("\n", "\n# ") + "\n\n";
        }

        return null;
    }

    public @NotNull Config withEntry(@NotNull String key, @NotNull Object defaultValue) {
        createEntry(key, defaultValue);

        return this;
    }

    public @NotNull Config withEntry(@NotNull String key, @NotNull Object defaultValue, @Nullable String comment) {
        createEntry(key, defaultValue, comment);

        return this;
    }

    public @NotNull Config withEntry(@NotNull String key, @NotNull Object defaultValue,
                                     @Nullable ConfigCommentProvider commentProvider) {
        createEntry(key, defaultValue, commentProvider);

        return this;
    }

    public @NotNull ConfigEntry createEntry(@NotNull String key, @Nullable Object defaultValue) {
        return createEntry(key, defaultValue, (ConfigCommentProvider) null);
    }

    public @NotNull ConfigEntry createEntry(@NotNull String key, @Nullable Object defaultValue, @Nullable String comment) {
        return createEntry(key, defaultValue, comment != null ? () -> comment : null);
    }

    public @NotNull ConfigEntry createEntry(@NotNull String key, @Nullable Object defaultValue,
                                            @Nullable ConfigCommentProvider commentProvider) {
        if (entries.containsKey(key)) {
            throw new IllegalArgumentException("Entry with the key '" + key + "' already exists on this config");
        }

        ConfigEntry cfgEntry = new ConfigEntry(key, defaultValue, commentProvider);
        entries.put(key, cfgEntry);

        return cfgEntry;
    }

    public @NotNull Config withCommentEntry(@NotNull String key, @NotNull String comment) {
        addCommentEntry(key, comment);

        return this;
    }

    public @NotNull Config withCommentEntry(@NotNull String key, @NotNull ConfigCommentProvider commentProvider) {
        addCommentEntry(key, commentProvider);

        return this;
    }

    public @NotNull ConfigEntry addCommentEntry(@NotNull String key, @NotNull String comment) {
        return addCommentEntry(key, () -> comment);
    }

    public @NotNull ConfigEntry addCommentEntry(@NotNull String key, @NotNull ConfigCommentProvider commentProvider) {
        if (commentEntries.containsKey(key)) {
            throw new IllegalArgumentException("CommentEntry with the key '" + key + "' already exists on this config");
        }

        ConfigEntry cfgEntry = new ConfigEntry(key, null, commentProvider);
        commentEntries.put(key, cfgEntry);

        return cfgEntry;
    }

    public @Nullable ConfigEntry getEntry(@NotNull String key) {
        return entries.get(key);
    }

    public @Nullable ConfigEntry getCommentEntry(@NotNull String key) {
        return commentEntries.get(key);
    }

    public boolean removeEntry(@NotNull String key) {
        return entries.remove(key) != null;
    }

    public boolean removeCommentEntry(@NotNull String key) {
        return commentEntries.remove(key) != null;
    }

    public boolean load() {
        try {
            loadWithException();
            return true;
        } catch (IOException ex) {
            LimeDevUtility.LOGGER.throwing(this.getClass().getName(), "load", ex);
        }

        return false;
    }

    public void loadWithException() throws IOException {
        if (this.file == null) throw new FileNotFoundException(ERR_NO_FILE);

        reset();

        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                Object yamlData = getSnakeYaml().load(reader);

                if (!(yamlData instanceof Map)) {
                    throw new IllegalStateException("The YAML file does not have a classical tree structure");
                }

                for (ConfigEntry cfgEntry : entries.values()) {
                    String[] nodeTree = cfgEntry.getKey().split("\\.");

                    Object obj = yamlData;

                    for (int i = 0; i < nodeTree.length - 1; ++i) {
                        String node = nodeTree[i];

                        obj = ((Map<?, ?>) obj).get(node);

                        if (!(obj instanceof Map)) {
                            obj = null;
                            break;
                        }
                    }

                    if (obj != null) {
                        Object value = ((Map<?, ?>) obj).get(nodeTree[nodeTree.length - 1]);

                        if (value != null) {
                            EntryValidator validator = cfgEntry.getEntryValidator();

                            if (validator != null && !validator.isValid(value)) {
                                LimeDevUtility.LOGGER.warning(() -> "Invalid value(=" + value +
                                        ") inside " + this.file.getName() + " for '" + cfgEntry.getKey() +
                                        "' (default value: '" + cfgEntry.getDefaultValue() + "')");
                            }

                            cfgEntry.setValue(value);
                        }
                    }
                }
            }
        }

        cfgListeners.forEach(ConfigListener::onLoad);
    }

    public boolean save() {
        try {
            saveWithException();
            return true;
        } catch (IOException ex) {
            LimeDevUtility.LOGGER.throwing(this.getClass().getName(), "save", ex);
        }

        return false;
    }

    public void saveWithException() throws IOException {
        if (this.file == null) throw new FileNotFoundException(ERR_NO_FILE);

        // In case #toString() throws an exception,
        // the file's content is not deleted
        String yamlStr = toString();

        try (FileWriter writer = new FileWriter(this.file)) {
            writer.append(yamlStr);
        }
    }

    public void backupFile() throws IOException {
        if (this.file == null) throw new FileNotFoundException(ERR_NO_FILE);
        if (!this.file.exists()) throw new FileNotFoundException("File '" + this.file.getAbsolutePath() +
                "' does not exist");

        Files.copy(this.file.toPath(), new File(this.file.getParentFile(),
                this.file.getName() + "-" + System.currentTimeMillis() + ".backup").toPath());  // TODO: Use "backup-" + getBaseName() + "-" + System.currMillis() + getFileExtension()
    }

    public Config addListener(@NotNull ConfigListener listener) {
        if (!cfgListeners.contains(listener)) {
            cfgListeners.add(listener);
        }

        return this;
    }

    public Config clearListeners() {
        cfgListeners.clear();

        return this;
    }

    public Config reset() {
        for (ConfigEntry cfgEntry : entries.values()) {
            cfgEntry.setValue(cfgEntry.getDefaultValue());
        }

        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String toString() {
        /* Construct a Node-Tree to generate the YAML from */
        Map<String, Object> dataRoot = new LinkedHashMap<>();

        for (ConfigEntry cfgEntry : entries.values()) {
            if (cfgEntry.getValue() == null) continue;

            String[] nodeTree = cfgEntry.getKey().split("\\.");

            Map<String, Object> nodeMap = dataRoot;

            for (int i = 0; i < nodeTree.length - 1; ++i) {
                String node = nodeTree[i];

                Object innerNodeMap = nodeMap.get(node);

                if (!(innerNodeMap instanceof Map)) {
                    if (innerNodeMap != null) {
                        throw new IllegalStateException("The ConfigEntry '" + cfgEntry.getKey() + "' conflicts with another entry");
                    }

                    innerNodeMap = new LinkedHashMap<>();

                    nodeMap.put(node, innerNodeMap);
                }

                nodeMap = (Map<String, Object>) innerNodeMap;
            }

            nodeMap.put(nodeTree[nodeTree.length - 1], cfgEntry.getValue());
        }

        String yamlStr = getSnakeYaml().dump(dataRoot);

        /* Fill the generated yamlStr with comments */

        // Inject node comments into yamlStr
        List<ConfigEntry> entryList = new ArrayList<>(entries.size() + commentEntries.size());
        entryList.addAll(entries.values());
        entryList.addAll(commentEntries.values());

        for (ConfigEntry cfgEntry : entryList) {
            String comment = cfgEntry.getFormattedComment();

            if (comment != null) {
                // Does the node exist?
                Map<String, Object> nodeMap = dataRoot;

                String[] nodeTree = cfgEntry.getKey().split("\\.");
                for (int i = 0; i < nodeTree.length - 1; ++i) {
                    String node = nodeTree[i];

                    Object innerNodeValue = nodeMap.get(node);

                    if (innerNodeValue == null) {
                        nodeMap = null;
                        break;
                    }

                    //noinspection unchecked
                    nodeMap = (Map<String, Object>) innerNodeValue;
                }

                if (nodeMap != null && nodeMap.containsKey(nodeTree[nodeTree.length - 1])) {
                    yamlStr = injectComment(yamlStr, cfgEntry.getKey(), comment);
                }
            }
        }

        /* Append file comment/header */
        String comment = getFormattedComment();
        if (comment != null) {
            return comment + yamlStr;
        }

        return yamlStr;
    }

    /**
     * <strong>
     * This method heavily relies on valid YAML and the key to exist!
     * <br>
     * Only tested with output from SnakeYAML
     * </strong>
     *
     * @param yaml    A string containing YAML (output from SnakeYAML)
     * @param key     The existing key, the comment is for
     * @param comment Already escaped comment string that is inserted above the {@code key} node
     *
     * @return A modified version of the given {@code yaml} string containing the comment
     */
    private String injectComment(String yaml, String key, String comment) {
        String[] nodes = key.split("\\.");

        int index = -1;

        for (int depth = 0; depth < nodes.length; ++depth) {
            if (depth > 0 && index == -1) break;

            index = yaml.indexOf(repeatString("  ", depth) + nodes[depth], index + 1);
        }

        if (index == -1) {
            throw new IllegalArgumentException("The given key(=" + key + ") does not exist!");
        }

        return yaml.substring(0, index) + ("\n" + comment).replace("\n", "\n" + repeatString("  ", nodes.length - 1)) + "\n" + yaml.substring(index);
    }

    private static Yaml getSnakeYaml() {
        if (yaml == null) {
            LoaderOptions yamlOptions = new LoaderOptions();
            yamlOptions.setAllowDuplicateKeys(false);
            yamlOptions.setWrappedToRootException(true);

            DumperOptions dumperOptions = new DumperOptions();
            dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

            yaml = new Yaml(new Constructor(yamlOptions), new Representer(), dumperOptions, yamlOptions);
        }

        return yaml;
    }

    @SuppressWarnings("SameParameterValue")
    private static String repeatString(String s, int count) {
        if (count < 0) throw new IllegalArgumentException();
        if (count == 0 || s.isEmpty()) return "";
        if (count == 1) return s;

        StringBuilder result = new StringBuilder(s.length() * count);

        for (int i = 0; i < count; ++i) {
            result.append(s);
        }

        return result.toString();
    }
}