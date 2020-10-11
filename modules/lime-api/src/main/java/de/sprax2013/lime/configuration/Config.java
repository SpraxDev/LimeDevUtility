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
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class provides an easy way to create YAML files with and high abstraction layer.<br>
 * A {@link Config} consists of {@link ConfigEntry}s with an unique identifier (=key).
 * <p>
 * The special thing about this class is, that you can have comments on *all* nodes (although that's against the spec)
 *
 * @see #saveWithException()
 * @see #loadWithException()
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class Config {
    private static final String ERR_NO_FILE = "You have to set a file for the configuration";

    private static Yaml yaml;

    private final Map<@NotNull String, @NotNull ConfigEntry> entries = new LinkedHashMap<>(),
            commentEntries = new HashMap<>();

    private final List<ConfigListener> cfgListeners = new ArrayList<>();

    private @Nullable File file;
    private @Nullable ConfigCommentProvider commentProvider;

    /**
     * @see #Config(File)
     * @see #Config(File, String)
     * @see #Config(File, ConfigCommentProvider)
     */
    public Config() {
        this(null);
    }

    /**
     * @param file The file used for loading and saving the {@link Config}
     *
     * @see #Config()
     * @see #Config(File, String)
     * @see #Config(File, ConfigCommentProvider)
     */
    public Config(@Nullable File file) {
        this(file, (ConfigCommentProvider) null);
    }

    /**
     * @param file    The file used for loading and saving the {@link Config}
     * @param comment The comment to use as the file header, or {@code null}
     *
     * @see #Config()
     * @see #Config(File)
     * @see #Config(File, ConfigCommentProvider)
     */
    public Config(@Nullable File file, @Nullable String comment) {
        this(file, () -> comment);
    }

    /**
     * @param file            The file used for loading and saving the {@link Config}
     * @param commentProvider The {@link ConfigCommentProvider} to use for the file header, or {@code null}
     *
     * @see #Config()
     * @see #Config(File)
     * @see #Config(File, String)
     */
    public Config(@Nullable File file, @Nullable ConfigCommentProvider commentProvider) {
        this.file = Objects.requireNonNull(file);
        this.commentProvider = commentProvider;
    }

    /**
     * @return The assigned file or {@code null}
     *
     * @see #setFile(File)
     * @see #Config(File)
     */
    public @Nullable File getFile() {
        return this.file;
    }

    /**
     * Sets the file used for loading and saving the {@link Config}<br>
     *
     * @param file The file to use
     *
     * @see #getFile()
     * @see #loadWithException()
     * @see #saveWithException()
     */
    public void setFile(@Nullable File file) {
        this.file = file;
    }

    /**
     * You can provide a {@link ConfigCommentProvider} that will be placed at the top of the file as a header.
     *
     * @param commentProvider The {@link ConfigCommentProvider} to use for the file header, or {@code null}
     *
     * @return The same {@link Config} for chaining
     *
     * @see ConfigCommentProvider
     * @see #getCommentProvider()
     */
    public Config setCommentProvider(@Nullable ConfigCommentProvider commentProvider) {
        this.commentProvider = commentProvider;

        return this;
    }

    /**
     * You can provide a {@link ConfigCommentProvider} that will be placed at the top of the file as a header.
     *
     * @return The {@link ConfigCommentProvider} assigned, or {@code null}
     *
     * @see ConfigCommentProvider
     * @see #setCommentProvider(ConfigCommentProvider)
     */
    public @Nullable ConfigCommentProvider getCommentProvider() {
        return this.commentProvider;
    }

    /**
     * This method adds the {@code #} to the beginning of the lines, to tell the YAML parser to ignore these lines<br>
     * This method might apply more complex structures according to the defined {@link CommentStyle}.
     *
     * @return Formatted comment {@link String}, or {@code null}
     *
     * @see CommentStyle
     * @see #setCommentProvider(ConfigCommentProvider)
     * @see #getCommentProvider()
     */
    public @Nullable String getFormattedComment() {
        String comment = this.commentProvider != null ? this.commentProvider.getComment() : null;

        if (comment != null && !comment.isEmpty()) {
            return "# " + comment.replace("\n", "\n# ") + "\n\n";
        }

        return null;
    }

    /**
     * Does the same as {@link #createEntry(String, Object)} but returning {@link Config}
     * for chaining instead of the created {@link ConfigEntry}.
     *
     * @param key          An unique identifier within the {@link Config}
     * @param defaultValue The default value for this entry
     *
     * @return The same {@link Config} for chaining
     *
     * @see ConfigEntry#getKey()
     */
    public @NotNull Config withEntry(@NotNull String key, @Nullable Object defaultValue) {
        createEntry(key, defaultValue);

        return this;
    }

    /**
     * Does the same as {@link #createEntry(String, Object, String)} but returning {@link Config}
     * for chaining instead of the created {@link ConfigEntry}.
     *
     * @param key          An unique identifier within the {@link Config}
     * @param defaultValue The default value for this entry
     * @param comment      The comment to use for this {@code key}, or {@code null}
     *
     * @return The same {@link Config} for chaining
     *
     * @see ConfigEntry#getKey()
     */
    public @NotNull Config withEntry(@NotNull String key, @Nullable Object defaultValue, @Nullable String comment) {
        createEntry(key, defaultValue, comment);

        return this;
    }

    /**
     * Does the same as {@link #createEntry(String, Object, ConfigCommentProvider)} but returning {@link Config}
     * for chaining instead of the created {@link ConfigEntry}.
     *
     * @param key             An unique identifier within the {@link Config}
     * @param defaultValue    The default value for this entry
     * @param commentProvider A {@link ConfigCommentProvider} that generates a comment or null
     *
     * @return The same {@link Config} for chaining
     *
     * @see ConfigEntry#getKey()
     * @see ConfigCommentProvider
     */
    public @NotNull Config withEntry(@NotNull String key, @Nullable Object defaultValue,
                                     @Nullable ConfigCommentProvider commentProvider) {
        createEntry(key, defaultValue, commentProvider);

        return this;
    }

    /**
     * @param key          An unique identifier within the {@link Config}
     * @param defaultValue The default value for this entry
     *
     * @return The created {@link ConfigEntry}
     *
     * @see ConfigEntry#getKey()
     * @see #createEntry(String, Object, String)
     * @see #createEntry(String, Object, ConfigCommentProvider)
     */
    public @NotNull ConfigEntry createEntry(@NotNull String key, @Nullable Object defaultValue) {
        return createEntry(key, defaultValue, (ConfigCommentProvider) null);
    }

    /**
     * @param key          An unique identifier within the {@link Config}
     * @param defaultValue The default value for this entry
     * @param comment      The comment to use for this {@code key}, or {@code null}
     *
     * @return The created {@link ConfigEntry}
     *
     * @see ConfigEntry#getKey()
     * @see #createEntry(String, Object)
     * @see #createEntry(String, Object, ConfigCommentProvider)
     */
    public @NotNull ConfigEntry createEntry(@NotNull String key, @Nullable Object defaultValue, @Nullable String comment) {
        return createEntry(key, defaultValue, comment != null ? () -> comment : null);
    }

    /**
     * @param key             An unique identifier within the {@link Config}
     * @param defaultValue    The default value for this entry
     * @param commentProvider A {@link ConfigCommentProvider} that generates a comment or null
     *
     * @return The created {@link ConfigEntry}
     *
     * @see ConfigEntry#getKey()
     * @see ConfigCommentProvider
     */
    public @NotNull ConfigEntry createEntry(@NotNull String key, @Nullable Object defaultValue,
                                            @Nullable ConfigCommentProvider commentProvider) {
        if (entries.containsKey(key)) {
            throw new IllegalArgumentException("Entry with the key '" + key + "' already exists on this config");
        }

        ConfigEntry cfgEntry = new ConfigEntry(key, defaultValue, commentProvider);
        entries.put(key, cfgEntry);

        return cfgEntry;
    }

    /**
     * Does the same as {@link #addCommentEntry(String, String)} but returning {@link Config}
     * for chaining instead of the created {@link ConfigEntry}.
     *
     * @param key     An unique identifier within the {@link Config}
     * @param comment The comment to use for this {@code key}
     *
     * @return The same {@link Config} for chaining
     *
     * @see ConfigEntry#getKey()
     * @see #withCommentEntry(String, ConfigCommentProvider)
     * @see #addCommentEntry(String, String)
     * @see #addCommentEntry(String, ConfigCommentProvider)
     */
    public @NotNull Config withCommentEntry(@NotNull String key, @NotNull String comment) {
        addCommentEntry(key, comment);

        return this;
    }

    /**
     * Does the same as {@link #addCommentEntry(String, ConfigCommentProvider)} but returning {@link Config}
     * for chaining instead of the created {@link ConfigEntry}.
     *
     * @param key             An unique identifier within the {@link Config}
     * @param commentProvider A {@link ConfigCommentProvider} that generates a comment
     *
     * @return The same {@link Config} for chaining
     *
     * @see ConfigEntry#getKey()
     * @see #withCommentEntry(String, String)
     * @see #addCommentEntry(String, String)
     * @see #addCommentEntry(String, ConfigCommentProvider)
     */
    public @NotNull Config withCommentEntry(@NotNull String key, @NotNull ConfigCommentProvider commentProvider) {
        addCommentEntry(key, commentProvider);

        return this;
    }

    /**
     * Creates an {@link ConfigEntry} solely for the purpose of the comment.<br>
     * With this, you can create comments that are in the middle of the given {@code key}-path.<br>
     * <p>
     * e.g. you have the keys {@code "Root.Profile.Username"} and {@code "Root.Profile.Age"}.<br>
     * You don't want do add comments to {@code "Username"} and {@code "Age"},
     * but instead above {@code "Profile"}. That's what this method is for.
     *
     * @param key     An unique identifier within the {@link Config}
     * @param comment The comment to use for this {@code key}
     *
     * @return The created {@link ConfigEntry}
     *
     * @see ConfigEntry#getKey()
     */
    public @NotNull ConfigEntry addCommentEntry(@NotNull String key, @NotNull String comment) {
        return addCommentEntry(key, () -> comment);
    }

    /**
     * Creates an {@link ConfigEntry} solely for the purpose of the comment.<br>
     * With this, you can create comments that are in the middle of the given {@code key}-path.<br>
     * <p>
     * e.g. you have the keys {@code "Root.Profile.Username"} and {@code "Root.Profile.Age"}.<br>
     * You don't want do add comments to {@code "Username"} and {@code "Age"},
     * but instead above {@code "Profile"}. That's what this method is for.
     *
     * @param key             An unique identifier within the {@link Config}
     * @param commentProvider A {@link ConfigCommentProvider} that generates a comment
     *
     * @return The created {@link ConfigEntry}
     *
     * @see ConfigEntry#getKey()
     */
    public @NotNull ConfigEntry addCommentEntry(@NotNull String key, @NotNull ConfigCommentProvider commentProvider) {
        if (commentEntries.containsKey(key)) {
            throw new IllegalArgumentException("CommentEntry with the key '" + key + "' already exists on this config");
        }

        ConfigEntry cfgEntry = new ConfigEntry(key, null, commentProvider);
        commentEntries.put(key, cfgEntry);

        return cfgEntry;
    }

    /**
     * @param key An unique identifier within the {@link Config}
     *
     * @return The {@link ConfigEntry} that has been found or {@code null}
     *
     * @see ConfigEntry#getKey()
     */
    public @Nullable ConfigEntry getEntry(@NotNull String key) {
        return entries.get(key);
    }

    /**
     * @param key An unique identifier within the {@link Config}
     *
     * @return The {@link ConfigEntry} that has been found or {@code null}
     *
     * @see ConfigEntry#getKey()
     */
    public @Nullable ConfigEntry getCommentEntry(@NotNull String key) {
        return commentEntries.get(key);
    }

    /**
     * Tries deleting a specific {@link ConfigEntry}.
     *
     * @param key An unique identifier within the {@link Config}
     *
     * @return true, if the key was found and removed, false otherwise
     *
     * @see #removeCommentEntry(String)
     */
    public boolean removeEntry(@NotNull String key) {
        return entries.remove(key) != null;
    }

    /**
     * Tries deleting a specific comment {@link ConfigEntry}.
     *
     * @param key An unique identifier within the {@link Config}
     *
     * @return true, if the key was found and removed, false otherwise
     *
     * @see #removeEntry(String)
     */
    public boolean removeCommentEntry(@NotNull String key) {
        return commentEntries.remove(key) != null;
    }

    /**
     * Calls {@link #loadWithException()} but returns a {@code boolean}
     * and prints any errors into the console
     *
     * @return True, if the file has been loaded successfully, false otherwise
     *
     * @see #loadWithException()
     */
    public boolean load() {
        try {
            loadWithException();
            return true;
        } catch (IOException ex) {
            LimeDevUtility.LOGGER.throwing(this.getClass().getName(), "load", ex);
        }

        return false;
    }

    /**
     * Calls {@link #reset()} and starts parsing the file using SneakYaml<br>
     * It requires the file to be in {@link org.yaml.snakeyaml.DumperOptions.FlowStyle#BLOCK}<br>
     * <p>
     * If a {@link ConfigEntry} has an {@link EntryValidator} assigned, it is called and might cause
     * printing a warning to the console<br>
     * <p>
     * If everything succeeds, all the {@link ConfigListener#onLoad()}s are called
     *
     * @throws IOException If {@link #getFile()} {@code == null} or thrown by {@link FileReader}
     * @see ConfigListener
     */
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

    /**
     * Calls {@link #saveWithException()} but returns a {@code boolean}
     * and prints any errors into the console
     *
     * @return True, if the file has been saved successfully, false otherwise
     *
     * @see #saveWithException()
     */
    public boolean save() {
        try {
            saveWithException();
            return true;
        } catch (IOException ex) {
            LimeDevUtility.LOGGER.throwing(this.getClass().getName(), "save", ex);
        }

        return false;
    }

    /**
     * Calls {@link #toString()} and writes it into the file at {@link #getFile()}
     *
     * @throws FileNotFoundException If {@link #getFile()} {@code == null}
     * @throws IOException           Thrown by {@link FileWriter#append(CharSequence)}
     */
    public void saveWithException() throws IOException {
        if (this.file == null) throw new FileNotFoundException(ERR_NO_FILE);

        // In case #toString() throws an exception,
        // the file's content is not deleted
        String yamlStr = toString();

        try (FileWriter writer = new FileWriter(this.file)) {
            writer.append(yamlStr);
        }
    }

    /**
     * Creates a copy of the configs {@link File}
     * with the name {@code "backup-${FileName}-}{@link System#currentTimeMillis()}{@code ${FileExtension}"}
     *
     * @throws FileNotFoundException If {@link #getFile()} {@code == null} or the file does not exist
     * @throws IOException           Thrown by {@link Files#copy(Path, Path, CopyOption...)}
     */
    public void backupFile() throws IOException {
        if (this.file == null) throw new FileNotFoundException(ERR_NO_FILE);
        if (!this.file.exists()) throw new FileNotFoundException("File '" + this.file.getAbsolutePath() +
                "' does not exist");

        Files.copy(this.file.toPath(), new File(this.file.getParentFile(),
                        "backup-" +
                                getFileBaseName(this.file.getName()) +
                                "-" + System.currentTimeMillis() +
                                "." + getFileExtension(this.file.getName())).toPath(),
                StandardCopyOption.COPY_ATTRIBUTES);
    }

    /**
     * Registers a {@link ConfigListener} that can be called by {@link Config}
     *
     * @param listener The listener to register
     *
     * @return The same {@link Config} for chaining
     *
     * @see ConfigListener
     */
    public @NotNull Config addListener(@NotNull ConfigListener listener) {
        if (!cfgListeners.contains(listener)) {
            cfgListeners.add(listener);
        }

        return this;
    }

    /**
     * Removes all registered listeners
     *
     * @return The same {@link Config} for chaining
     */
    public @NotNull Config clearListeners() {
        cfgListeners.clear();

        return this;
    }

    /**
     * Sets all {@link ConfigEntry} values to {@link ConfigEntry#getDefaultValue()}
     *
     * @return The same {@link Config} for chaining
     */
    public @NotNull Config reset() {
        for (ConfigEntry cfgEntry : entries.values()) {
            cfgEntry.setValue(cfgEntry.getDefaultValue());
        }

        return this;
    }

    /**
     * All {@link ConfigEntry}s are iterated and a {@link LinkedHashMap} is populated to build a tree-like
     * structure that can be fed into SnakeYaml. If {@link ConfigEntry#getValue()} {@code == null}, it is skipped<br>
     * <p>
     * As soon as the YAML string has been created, the comments are injected using {@link #injectComment(String, String, String)}.
     *
     * @return A YAML formatted string with all the {@link ConfigEntry}s and comments, if any
     *
     * @throws IllegalStateException If a {@link ConfigEntry} is conflicting with another one (same {@code key}?)
     */
    @SuppressWarnings("unchecked")
    @Override
    public @NotNull String toString() {
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
    private @NotNull String injectComment(String yaml, String key, String comment) {
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

    /**
     * This instance disallows duplicate keys,
     * wraps Exceptions into {@link org.yaml.snakeyaml.error.YAMLException}s
     * and it's output is set to {@link org.yaml.snakeyaml.DumperOptions.FlowStyle#BLOCK}
     *
     * @return The SnakeYaml instance that should be used for parsing and writing YAML
     */
    private static @NotNull Yaml getSnakeYaml() {
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

    /**
     * Very similar to {@link String#repeat(int)} from Java 11.<br>
     * It takes the {@link String} {@code str} and repeats it {@code count} times.
     *
     * @param str   The {@link String} that should be repeated
     * @param count How often the given {@link String} should be repeated
     *
     * @return The resulting {@link String}
     *
     * @throws IllegalArgumentException If {@code count < 0}
     */
    @SuppressWarnings("SameParameterValue")
    private static @NotNull String repeatString(String str, int count) {
        if (count < 0) throw new IllegalArgumentException();
        if (count == 0 || str.isEmpty()) return "";
        if (count == 1) return str;

        StringBuilder result = new StringBuilder(str.length() * count);

        for (int i = 0; i < count; ++i) {
            result.append(str);
        }

        return result.toString();
    }

    private static @NotNull String getFileBaseName(@NotNull String fileName) {
        int index = fileName.lastIndexOf('.');

        return index == -1 ? fileName : fileName.substring(0, index);
    }

    private static @NotNull String getFileExtension(@NotNull String fileName) {
        int index = fileName.lastIndexOf('.');

        return index == -1 ? fileName : fileName.substring(index + 1);
    }
}