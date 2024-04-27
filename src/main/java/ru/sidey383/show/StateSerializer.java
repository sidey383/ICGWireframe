package ru.sidey383.show;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.sidey383.editor.ErrorDialog;
import ru.sidey383.model.data.FigureInfoRecord;
import ru.sidey383.model.math.QuaternionRotation;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class StateSerializer {

    private final ObjectMapper mapper = new ObjectMapper();

    private final JFileChooser fileChooser;

    private final ErrorDialog errorDialog = new ErrorDialog(() -> "File load error", () -> "Can't load this file");

    public StateSerializer() {
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("State data", "json"));
    }

    public State load(Component parent) {
        int res = fileChooser.showOpenDialog(parent);
        if (res == JFileChooser.APPROVE_OPTION) {
            File in = new File(fileChooser.getSelectedFile().getAbsolutePath());
            try {
                State s = mapper.readValue(in, State.class);
                if (s != null)
                    return s;
            } catch (Exception e) {
                errorDialog.show();
            }
        }
        return null;
    }

    public void save(Component parent, State state) {
        if (state == null)
            return;
        int res = fileChooser.showSaveDialog(parent);
        if (res == JFileChooser.APPROVE_OPTION) {
            File out = new File(fileChooser.getSelectedFile().getAbsolutePath());
            if (!out.getName().toLowerCase().endsWith(".json")) {
                out = new File(out.getParentFile(), out.getName() + ".json");
            }
            try {
                mapper.writer().writeValue(out, state);
            } catch (IOException e) {
                errorDialog.show();
            }
        }
    }

    public record State(FigureInfoRecord figure, QuaternionRotation rotation, Double fov) { }

}
