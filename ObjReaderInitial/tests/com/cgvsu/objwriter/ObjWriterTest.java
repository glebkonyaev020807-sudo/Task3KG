package com.cgvsu.objwriter;

import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import com.cgvsu.objreader.ObjReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class ObjWriterTest {
    @TempDir Path directory;

    @Test
    void allFaceFormatsRoundTrip() throws Exception {
        String data = "v -1.5 0.0 0.0\nv 0.0 1.0 0.0\nv 0.0 0.0 1.0\nv 2.0 1.0 0.0\n"
                + "vt 0.5 0.25\nvn 0.0 0.0 1.0\n"
                + "f 4 2 3 1\nf 3/1 2/1 1/1\nf 1//1 3//1 2//1\nf 2/1/1 1/1/1 3/1/1\n";
        Path file = directory.resolve("моя модель.obj");
        ObjWriter.write(ObjReader.read(data), file);
        String saved = Files.readString(file);
        assertEquals(data, saved);
        assertEquals(4, ObjReader.read(saved).polygons.size());
    }

    @Test
    void emptyModel() throws Exception {
        Path file = directory.resolve("empty.obj");
        ObjWriter.write(new Model(), file);
        assertEquals("", Files.readString(file));
    }

    @Test
    void rejectsIncompletePolygon() {
        Model model = new Model();
        model.polygons.add(new Polygon());
        assertThrows(IllegalArgumentException.class,
                () -> ObjWriter.write(model, directory.resolve("invalid.obj")));
    }
}
