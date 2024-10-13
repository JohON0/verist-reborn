/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.utils.shader.shaders;

import im.expensive.utils.shader.IShader;

public class Mainmenu
implements IShader {
    @Override
    public String glsl() {
        return " #ifdef GL_ES\n precision mediump float;\n #endif\n\n uniform float time;\n uniform vec2 resolution;\n uniform sampler2D backbuffer;\n\n #define PI 3.14159\n\n void main(){\n \tvec2 p = (gl_FragCoord.xy - 0.5 * resolution) / min(resolution.x, resolution.y);\n \tvec2 t = vec2(gl_FragCoord.xy / resolution);\n\n \tvec3 c = vec3(0);\n\n \tfor(int i = 0; i < 20; i++) {\n \t\tfloat t = 0.4 * PI * float(i) / 3.0 * time * 0.5;\n \t\tfloat x = cos(3.0*t);\n \t\tfloat y = sin(4.0*t);\n \t\tvec2 o = 0.40 * vec2(x, y);\n \t\tfloat r = fract(x);\n \t\tfloat g = 1.0 - r;\n \t\tc += 0.01 / (length(p-o)) * vec3(r, g, 0.9);\n \t}\n\n \tgl_FragColor = vec4(c, 1);\n }\n";
    }
}

