/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.utils.shader;

import im.expensive.utils.client.IMinecraft;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL20;

public class ShaderHandler
implements IMinecraft {
    private final int programID;
    private String shaderMainMenu = "#ifdef GL_ES\nprecision mediump float;\n#endif\n\n#extension GL_OES_standard_derivatives : enable\n\nuniform vec2 resolution;\nuniform float time;\n\n/*\n* @author Hazsi (kinda)\n*/\nmat2 m(float a) {\n    float c=cos(a), s=sin(a);\n    return mat2(c,-s,s,c);\n}\n\nfloat map(vec3 p) {\n    p.xz *= m(time * 0.4);p.xy*= m(time * 0.1);\n    vec3 q = p * 2.0 + time;\n    return length(p+vec3(sin(time * 0.7))) * log(length(p) + 1.0) + sin(q.x + sin(q.z + sin(q.y))) * 0.5 - 1.0;\n}\n\nvoid main() {\n    vec2 a = gl_FragCoord.xy / resolution.y - vec2(0.9, 0.5);\n    vec3 cl = vec3(0.0);\n    float d = 2.5;\n\n    for (int i = 0; i <= 5; i++) {\n        vec3 p = vec3(0, 0, 4.0) + normalize(vec3(a, -1.0)) * d;\n        float rz = map(p);\n        float f =  clamp((rz - map(p + 0.1)) * 0.5, -0.1, 1.0);\n        vec3 l = vec3(0.1, 0.3, 0.4) + vec3(5.0, 2.5, 3.0) * f;\n        cl = cl * l + smoothstep(2.5, 0.0, rz) * 0.6 * l;\n        d += min(rz, 1.0);\n    }\n\n    gl_FragColor = vec4(cl, 1.0);\n}";

    public ShaderHandler(String fragmentShaderLoc, String vertexShaderLoc) {
        int program = GL20.glCreateProgram();
        try {
            GL20.glAttachShader(program, switch (fragmentShaderLoc) {
                case "shaderMainMenu" -> this.createShader(new ByteArrayInputStream(this.shaderMainMenu.getBytes()), 35632);
                default -> this.createShader(mc.getResourceManager().getResource(new ResourceLocation(fragmentShaderLoc)).getInputStream(), 35632);
            });
            int vertexShaderID = this.createShader(mc.getResourceManager().getResource(new ResourceLocation(vertexShaderLoc)).getInputStream(), 35633);
            GL20.glAttachShader(program, vertexShaderID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        GL20.glLinkProgram(program);
        int status2 = GL20.glGetProgrami(program, 35714);
        if (status2 == 0) {
            throw new IllegalStateException("Shader failed to link!");
        }
        this.programID = program;
    }

    public void init() {
        GL20.glUseProgram(this.programID);
    }

    public void unload() {
        GL20.glUseProgram(0);
    }

    public ShaderHandler(String fragmentShaderLoc) {
        this(fragmentShaderLoc, new ResourceLocation("minecraft", "expensive/shader/vertex.vsh").getPath());
    }

    public void setUniformf(String name, float ... args2) {
        int loc = GL20.glGetUniformLocation(this.programID, name);
        switch (args2.length) {
            case 1: {
                GL20.glUniform1f(loc, args2[0]);
                break;
            }
            case 2: {
                GL20.glUniform2f(loc, args2[0], args2[1]);
                break;
            }
            case 3: {
                GL20.glUniform3f(loc, args2[0], args2[1], args2[2]);
                break;
            }
            case 4: {
                GL20.glUniform4f(loc, args2[0], args2[1], args2[2], args2[3]);
            }
        }
    }

    public void setUniformi(String name, int ... args2) {
        int loc = GL20.glGetUniformLocation(this.programID, name);
        if (args2.length > 1) {
            GL20.glUniform2i(loc, args2[0], args2[1]);
        } else {
            GL20.glUniform1i(loc, args2[0]);
        }
    }

    public static void drawQuads(float x, float y, float width, float height) {
        GL20.glBegin(7);
        GL20.glTexCoord2f(0.0f, 0.0f);
        GL20.glVertex2f(x, y);
        GL20.glTexCoord2f(0.0f, 1.0f);
        GL20.glVertex2f(x, y + height);
        GL20.glTexCoord2f(1.0f, 1.0f);
        GL20.glVertex2f(x + width, y + height);
        GL20.glTexCoord2f(1.0f, 0.0f);
        GL20.glVertex2f(x + width, y);
        GL20.glEnd();
    }

    public static void drawQuads() {
        float width = mc.getMainWindow().getScaledWidth();
        float height = mc.getMainWindow().getScaledHeight();
        GL20.glBegin(7);
        GL20.glTexCoord2f(0.0f, 1.0f);
        GL20.glVertex2f(0.0f, 0.0f);
        GL20.glTexCoord2f(0.0f, 0.0f);
        GL20.glVertex2f(0.0f, height);
        GL20.glTexCoord2f(1.0f, 0.0f);
        GL20.glVertex2f(width, height);
        GL20.glTexCoord2f(1.0f, 1.0f);
        GL20.glVertex2f(width, 0.0f);
        GL20.glEnd();
    }

    public int createShader(InputStream inputStream, int shaderType) {
        int shader = GL20.glCreateShader(shaderType);
        GL20.glShaderSource(shader, (CharSequence)ShaderHandler.readInputStream(inputStream));
        GL20.glCompileShader(shader);
        if (GL20.glGetShaderi(shader, 35713) == 0) {
            System.out.println(GL20.glGetShaderInfoLog(shader, 4096));
            throw new IllegalStateException(String.format("Shader (%s) failed to compile!", shaderType));
        }
        return shader;
    }

    public static String readInputStream(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            String line;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}

