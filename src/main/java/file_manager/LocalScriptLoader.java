package file_manager;

import bot_parameters.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class LocalScriptLoader {

    public final List<Script> getLocalScripts() {

        final List<Script> localScripts = new ArrayList<>();

        final String scriptsDirPath = Paths.get(System.getProperty("user.home"), "OSBot", "Scripts").toString();
        final File scriptsDir = new File(scriptsDirPath);

        if (!scriptsDir.isDirectory()) return localScripts;

        final File[] jarFiles = scriptsDir.listFiles((dir, name) -> name.endsWith(".jar"));

        if (jarFiles == null) return localScripts;

        for (final File file : jarFiles) {
            try {
                URL url = file.toURI().toURL();
                ClassLoader classLoader = new URLClassLoader(new URL[]{url});
                JarFile jarFile = new JarFile(file.getAbsolutePath());
                addScriptsFromJar(jarFile, classLoader, localScripts);
            } catch (Exception ignored) {}
        }
        return localScripts;
    }

    private void addScriptsFromJar(final JarFile jarFile, final ClassLoader classLoader, final List<Script> localScripts) {
        System.out.println(String.format("Loading jar: '%s'", jarFile.getName()));
        Enumeration entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = (JarEntry) entries.nextElement();
            String name = entry.getName();
            if (name.endsWith(".class")) {
                getScriptFromClass(classLoader, name).ifPresent(localScripts::add);
            }
        }
    }

    private Optional<Script> getScriptFromClass(final ClassLoader classLoader, final String className) {
        try {
            Class cls = classLoader.loadClass(className.replace('/', '.').replace(".class", ""));
            Annotation scriptManifest = cls.getAnnotation(ScriptManifest.class);
            if (scriptManifest != null) {
                return Optional.of(new Script(((ScriptManifest) scriptManifest).name(), "", true));
            }
        } catch (Exception | NoClassDefFoundError ignored) {}
        return Optional.empty();
    }
}
