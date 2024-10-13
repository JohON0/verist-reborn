/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.lwjgl.opengl.GL20;

public class ShaderUtil1 {
    private static int blurShaderProgram;
    private static int vertexShader;
    private static int fragmentShader;

    public static void initShaders() {
        blurShaderProgram = GL20.glCreateProgram();
        vertexShader = ShaderUtil1.loadShader("path/to/vertex_shader.glsl", 35633);
        fragmentShader = ShaderUtil1.loadShader("path/to/fragment_shader.glsl", 35632);
        GL20.glAttachShader(blurShaderProgram, vertexShader);
        GL20.glAttachShader(blurShaderProgram, fragmentShader);
        GL20.glLinkProgram(blurShaderProgram);
        GL20.glValidateProgram(blurShaderProgram);
    }

    private static int loadShader(String file, int type) {
        StringBuilder shaderSource = new StringBuilder();
        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, (CharSequence)shaderSource);
        GL20.glCompileShader(shaderID);
        if (GL20.glGetShaderi(shaderID, 35713) == 0) {
            System.err.println("Failed to compile shader: " + GL20.glGetShaderInfoLog(shaderID, 500));
            return 0;
        }
        return shaderID;
    }

    public static void enableFogBlurShader() {
        GL20.glUseProgram(blurShaderProgram);
        int blurRadiusLocation = GL20.glGetUniformLocation(blurShaderProgram, "blurRadius");
        GL20.glUniform1f(blurRadiusLocation, 5.0f);
    }

    public static void disableFogBlurShader() {
        GL20.glUseProgram(0);
    }
}

