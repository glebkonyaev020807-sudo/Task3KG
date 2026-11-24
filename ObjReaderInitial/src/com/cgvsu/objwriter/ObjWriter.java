package com.cgvsu.objwriter;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ObjWriter {
    public static void write(Model model, Path file) throws IOException {
        StringBuilder text = new StringBuilder();
        for (Vector3f v : model.vertices) {
            text.append("v ").append(v.getX()).append(' ').append(v.getY()).append(' ').append(v.getZ()).append('\n');
        }
        for (Vector2f v : model.textureVertices) {
            text.append("vt ").append(v.getX()).append(' ').append(v.getY()).append('\n');
        }
        for (Vector3f n : model.normals) {
            text.append("vn ").append(n.getX()).append(' ').append(n.getY()).append(' ').append(n.getZ()).append('\n');
        }
        for (Polygon polygon : model.polygons) {
            appendFace(text, polygon);
        }
        Files.writeString(file, text);
    }

    private static void appendFace(StringBuilder text, Polygon polygon) {
        var vertices = polygon.getVertexIndices();
        var textures = polygon.getTextureVertexIndices();
        var normals = polygon.getNormalIndices();
        if (vertices.size() < 3 || (!textures.isEmpty() && textures.size() != vertices.size())
                || (!normals.isEmpty() && normals.size() != vertices.size())) {
            throw new IllegalArgumentException("Некорректные списки индексов полигона");
        }
        text.append('f');
        for (int i = 0; i < vertices.size(); i++) {
            // В OBJ номера начинаются с единицы.
            text.append(' ').append(vertices.get(i) + 1);
            if (!textures.isEmpty() || !normals.isEmpty()) {
                text.append('/');
                if (!textures.isEmpty()) {
                    text.append(textures.get(i) + 1);
                }
                if (!normals.isEmpty()) {
                    text.append('/').append(normals.get(i) + 1);
                }
            }
        }
        text.append('\n');
    }
}
