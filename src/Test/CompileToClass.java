package Test;

import javax.tools.*;
import java.io.*;
import java.util.*;

import java.nio.file.*;
import java.util.jar.*;

public class CompileToClass {
    public static void main(String[] args) {
        String sourceDir = "src";      // Source directory
        String buildDir = "testOut";    // Directory for compiled files
        String resourcesDir = "src/code"; // Directory for resources
        String jarFileName = "test.jar";


        try {
            // Step 1: Compile Java files
            compileJavaFiles(sourceDir, buildDir);

            // Step 2: Copy resources to build directory
            copyResources(new File(resourcesDir), new File(buildDir));

            // Step 3: Create an executable JAR file
            createJarFile(buildDir, jarFileName, "functions.Main");

            System.out.println("Compilation and JAR packaging complete! Created: " + jarFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Compiles all .java files in the source directory to the build directory
    private static void compileJavaFiles(String sourceDir, String buildDir) throws IOException {
        // Ensure build directory exists
        new File(buildDir).mkdirs();

        // Collect .java files
        List<File> javaFiles = getJavaFiles(new File(sourceDir));
        if (javaFiles.isEmpty()) {
            throw new IllegalArgumentException("No Java files found in " + sourceDir);
        }

        // Use JavaCompiler to compile
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException("No Java compiler available. Are you running on a JDK?");
        }

        List<String> filePaths = new ArrayList<>();
        for (File file : javaFiles) {
            filePaths.add(file.getPath());
        }

        // Compile with -d flag to set output directory
        List<String> options = Arrays.asList("-d", buildDir);
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
            Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromStrings(filePaths);
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, options, null, compilationUnits);

            if (!task.call()) {
                throw new IllegalStateException("Compilation failed.");
            }
        }
    }

    // Recursively collects all .java files in the directory
    private static List<File> getJavaFiles(File dir) {
        List<File> javaFiles = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    javaFiles.addAll(getJavaFiles(file));
                } else if (file.getName().endsWith(".java")) {
                    javaFiles.add(file);
                }
            }
        }
        return javaFiles;
    }

    // Copies resources from one directory to another
    private static void copyResources(File sourceDir, File targetDir) throws IOException {
        if (!sourceDir.exists()) return; // Skip if no resources

        Files.walk(sourceDir.toPath()).forEach(source -> {
            Path target = targetDir.toPath().resolve(sourceDir.toPath().relativize(source));
            try {
                Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // Creates an executable JAR file
    private static void createJarFile(String buildDir, String jarFileName, String mainClass) throws IOException {
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarFileName))) {
            // Add MANIFEST.MF
            jos.putNextEntry(new JarEntry("META-INF/"));
            jos.putNextEntry(new JarEntry("META-INF/MANIFEST.MF"));
            String manifest = "Manifest-Version: 1.0\nMain-Class: " + mainClass + "\n";
            jos.write(manifest.getBytes());
            jos.closeEntry();

            // Add files from build directory to JAR
            addFilesToJar(new File(buildDir), "", jos);
        }
    }

    // Recursively adds files to a JAR
    private static void addFilesToJar(File source, String parentPath, JarOutputStream jos) throws IOException {
        String entryName = parentPath + source.getName();
        if (source.isDirectory()) {
            if (!entryName.isEmpty()) {
                jos.putNextEntry(new JarEntry(entryName + "/"));
                jos.closeEntry();
            }
            for (File file : Objects.requireNonNull(source.listFiles())) {
                addFilesToJar(file, entryName + "/", jos);
            }
        } else {
            jos.putNextEntry(new JarEntry(entryName));
            Files.copy(source.toPath(), jos);
            jos.closeEntry();
        }
    }
}

