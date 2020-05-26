package org.cn2b2t.proxy.utils;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;

public class FileConfig {

    private File file;
    private Configuration config;

    public FileConfig(final Plugin plugin) {
        this(plugin, "config.yml");
    }

    public <T> T get(final String path, final T def) {
        return (T) this.config.get(path, (Object) def);
    }

    public boolean contains(final String path) {
        return this.config.contains(path);
    }

    public Object get(final String path) {
        return this.config.get(path);
    }

    public Object getDefault(final String path) {
        return this.config.getDefault(path);
    }

    public void set(final String path, final Object value) {
        this.config.set(path, value);
    }

    public Configuration getSection(final String path) {
        return this.config.getSection(path);
    }

    public Collection<String> getKeys() {
        return this.config.getKeys();
    }

    public byte getByte(final String path) {
        return this.config.getByte(path);
    }

    public byte getByte(final String path, final byte def) {
        return this.config.getByte(path, def);
    }

    public List<Byte> getByteList(final String path) {
        return this.config.getByteList(path);
    }

    public short getShort(final String path) {
        return this.config.getShort(path);
    }

    public short getShort(final String path, final short def) {
        return this.config.getShort(path, def);
    }

    public List<Short> getShortList(final String path) {
        return this.config.getShortList(path);
    }

    public int getInt(final String path) {
        return this.config.getInt(path);
    }

    public int getInt(final String path, final int def) {
        return this.config.getInt(path, def);
    }

    public List<Integer> getIntList(final String path) {
        return this.config.getIntList(path);
    }

    public long getLong(final String path) {
        return this.config.getLong(path);
    }

    public long getLong(final String path, final long def) {
        return this.config.getLong(path, def);
    }

    public List<Long> getLongList(final String path) {
        return this.config.getLongList(path);
    }

    public float getFloat(final String path) {
        return this.config.getFloat(path);
    }

    public float getFloat(final String path, final float def) {
        return this.config.getFloat(path, def);
    }

    public List<Float> getFloatList(final String path) {
        return this.config.getFloatList(path);
    }

    public double getDouble(final String path) {
        return this.config.getDouble(path);
    }

    public double getDouble(final String path, final double def) {
        return this.config.getDouble(path, def);
    }

    public List<Double> getDoubleList(final String path) {
        return this.config.getDoubleList(path);
    }

    public boolean getBoolean(final String path) {
        return this.config.getBoolean(path);
    }

    public boolean getBoolean(final String path, final boolean def) {
        return this.config.getBoolean(path, def);
    }

    public List<Boolean> getBooleanList(final String path) {
        return this.config.getBooleanList(path);
    }

    public char getChar(final String path) {
        return this.config.getChar(path);
    }

    public char getChar(final String path, final char def) {
        return this.config.getChar(path, def);
    }

    public List<Character> getCharList(final String path) {
        return this.config.getCharList(path);
    }

    public String getString(final String path) {
        return this.config.getString(path);
    }

    public String getString(final String path, final String def) {
        return this.config.getString(path, def);
    }

    public List<String> getStringList(final String path) {
        return this.config.getStringList(path);
    }

    public List<?> getList(final String path) {
        return this.config.getList(path);
    }

    public List<?> getList(final String path, final List<?> def) {
        return this.config.getList(path, def);
    }

    public FileConfig(final Plugin plugin, final String name) {
        this.file = new File(plugin.getDataFolder(), name);
        try {
            if (!this.file.exists()) {
                if (!this.file.getParentFile().exists()) {
                    this.file.getParentFile().mkdirs();
                }
                Files.copy(plugin.getResourceAsStream(name), this.file.toPath());
            }
            this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
        } catch (IOException e) {
            System.out.println("\u914d\u7f6e\u6587\u4ef6\u8bfb\u53d6\u5931\u8d25!");
            e.printStackTrace();
        }
    }

    public Configuration getConfig() {
        return this.config;
    }

    public void save() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(this.config, this.file);
        } catch (IOException e) {
            System.out.println("\u914d\u7f6e\u6587\u4ef6\u4fdd\u5b58\u5931\u8d25\uff01");
            e.printStackTrace();
        }
    }

    public void reload() {
        try {
            this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
        } catch (IOException e) {
            System.out.println("\u914d\u7f6e\u6587\u4ef6\u8bfb\u53d6\u5931\u8d25!");
            e.printStackTrace();
        }
    }
}
