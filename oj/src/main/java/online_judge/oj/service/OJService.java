package online_judge.oj.service;

import online_judge.oj.model.CodeRequestModel;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;

@Service
public class OJService {

    public String executeCode(CodeRequestModel codeRequestModel) throws Exception {
        switch (codeRequestModel.getLanguage()){
            case "python":{
              return  runPython(codeRequestModel.getCode());
            }
            case "java":{
                return  runJava(codeRequestModel.getCode());
            }
            case "c++":
            case "cpp":
                return  runCpp(codeRequestModel.getCode());
            default:{
                throw new IllegalArgumentException("Unsupported language: " + codeRequestModel.getLanguage());
            }
        }
    }
    private String runPython(String code) throws Exception {
        File file = File.createTempFile("script", ".py");
        Files.write(file.toPath(), code.getBytes());

        return runCommand("python3 " + file.getAbsolutePath());
    }

    private String runJava(String code) throws Exception {
        String className = "Main";
        File file = new File(System.getProperty("java.io.tmpdir"), className + ".java");
        Files.write(file.toPath(), code.getBytes());
        System.out.println(file);
        String compile = runCommand("javac " + file.getAbsolutePath());
        if (!compile.isEmpty()) return compile;

        return runCommand("java -cp " + file.getParent() + " " + className);
    }

    private String runCpp(String code) throws Exception {
        File file = File.createTempFile("program", ".cpp");
        File outFile = new File(file.getParent(), "a.out");
        Files.write(file.toPath(), code.getBytes());

        String compile = runCommand("g++ " + file.getAbsolutePath() + " -o " + outFile.getAbsolutePath());
        if (!compile.isEmpty()) return compile;

        return runCommand(outFile.getAbsolutePath());
    }
    private String runCommand(String command) throws Exception {
        String os = System.getProperty("os.name").toLowerCase();
        ProcessBuilder pb;

        if (os.contains("win")) {
            pb = new ProcessBuilder("cmd.exe", "/c", command); // Windows
        } else {
            pb = new ProcessBuilder("bash", "-c", command);    // Linux/macOS
        }
        pb.redirectErrorStream(true);
        Process process = pb.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) result.append(line).append("\n");

        process.waitFor();
        return result.toString().trim();
    }

}
