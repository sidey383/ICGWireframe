package ru.sidey383;

import ru.sidey383.model.data.FigureInfoRecord;
import ru.sidey383.model.data.PointRecord;
import ru.sidey383.model.figure.FigureVertexContainer;
import ru.sidey383.model.math.Matrix;
import ru.sidey383.model.math.MatrixTransformation;
import ru.sidey383.model.math.QuaternionRotation;
import ru.sidey383.show.HelpDialog;
import ru.sidey383.show.ShowFrame;
import ru.sidey383.show.ShowFrameInteraction;
import ru.sidey383.show.StateSerializer;
import ru.sidey383.show.painter.PerspectiveLinesPainter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainFrame extends MyFrame {

    public static void main(String[] args) {
        new MainFrame();
    }

    private final StateSerializer stateSerializer = new StateSerializer();

    private final ShowFrame<PerspectiveLinesPainter> showFrame;

    private final EditorFrame editorFrame;

    private final HelpDialog helpDialog = new HelpDialog();

    private FigureInfoRecord figureInfo = new FigureInfoRecord(List.of(
            new PointRecord(-2, 5),
            new PointRecord(-1, 3),
            new PointRecord(0, 0),
            new PointRecord(1, 3),
            new PointRecord(2, 5)
    ),
            10,
            5,
            3
    );

    private final ActionListener editorAction = this::editorAction;

    private final ActionListener clearAngleAction = this::clearAngleAction;

    private final ActionListener saveAction = this::saveAction;

    private final ActionListener loadAction = this::loadAction;

    private final ActionListener helpAction = this::helpAction;

    public MainFrame() {
        super("ICGWireframe");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setFocusable(true);

        showFrame = new ShowFrame<>(new PerspectiveLinesPainter());
        ShowFrameInteraction showFrameInteraction = new ShowFrameInteraction(showFrame);
        showFrame.addMouseListener(showFrameInteraction);
        showFrame.addMouseMotionListener(showFrameInteraction);
        showFrame.addMouseWheelListener(showFrameInteraction);

        editorFrame = new EditorFrame(figureInfo, this::setFigureInfo);

        add(showFrame, BorderLayout.CENTER);
        add(createToolBar(), BorderLayout.NORTH);
        setJMenuBar(createJMenuBar());

        setFigureInfo(figureInfo);

        pack();
        setVisible(true);
    }

    private void loadAction(ActionEvent e) {
        StateSerializer.State state = stateSerializer.load(this);
        if (state == null)
            return;
        editorFrame.setVisible(false);
        if (state.figure() != null)
            setFigureInfo(state.figure());
        if (state.rotation() != null) {
            Matrix m = state.rotation().toRotationMatrix();
            showFrame.setRotation(m);
        }
        if (state.fov() != null) {
            showFrame.getPainter().setFov(state.fov());
        }
    }

    private void helpAction(ActionEvent e) {
        helpDialog.showHelp();
    }

    private void saveAction(ActionEvent e) {
        stateSerializer.save(
                this,
                new StateSerializer.State(
                        figureInfo,
                        QuaternionRotation.fromMatrix(showFrame.getRotation()),
                        showFrame.getPainter().getFov()
                )
        );
    }

    private void editorAction(ActionEvent e) {
        if (!editorFrame.isShowing()) {
            editorFrame.setVisible(true);
            editorFrame.setFigureInfo(figureInfo);
        } else {
            editorFrame.setState(Frame.NORMAL);
            if (!editorFrame.isActive()) {
                editorFrame.setVisible(true);
            }
            editorFrame.setAlwaysOnTop(true);
            editorFrame.setAlwaysOnTop(false);
        }
    }

    private void clearAngleAction(ActionEvent e) {
        showFrame.setRotation(MatrixTransformation.getNoTransformation());
    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();

        JButton editorJButton = new JButton(loadButtonIcon("/icon/edit.png"));
        editorJButton.setToolTipText("Open figure editor");
        editorJButton.addActionListener(editorAction);
        applyBorder(editorJButton);

        JButton clearAngleJButton = new JButton(loadButtonIcon("/icon/rotate.png"));
        clearAngleJButton.setToolTipText("Remove the rotations of the figure");
        clearAngleJButton.addActionListener(clearAngleAction);
        applyBorder(clearAngleJButton);

        JButton saveJButton = new JButton(loadButtonIcon("/icon/save.png"));
        saveJButton.setToolTipText("Save current figure");
        saveJButton.addActionListener(saveAction);
        applyBorder(saveJButton);

        JButton loadJButton = new JButton(loadButtonIcon("/icon/load.png"));
        loadJButton.setToolTipText("Load figure from file");
        loadJButton.addActionListener(loadAction);
        applyBorder(loadJButton);

        JButton helpJButton = new JButton(loadButtonIcon("/icon/help.png"));
        helpJButton.setToolTipText("Add program info");
        helpJButton.addActionListener(helpAction);
        applyBorder(helpJButton);

        toolBar.add(loadJButton);
        toolBar.add(saveJButton);
        toolBar.addSeparator();
        toolBar.add(editorJButton);
        toolBar.add(clearAngleJButton);
        toolBar.addSeparator();
        toolBar.add(helpJButton);
        toolBar.setFloatable(false);
        return toolBar;
    }

    private void applyBorder(AbstractButton button) {
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setMargin(new Insets(1, 1, 1, 1));
    }

    private JMenuBar createJMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);

        JMenu file = new JMenu("File");
        JMenuItem load = new JMenuItem("Open");
        JMenuItem save = new JMenuItem("Save");
        load.addActionListener(loadAction);
        save.addActionListener(saveAction);
        file.add(load);
        file.add(save);
        menuBar.add(file);

        JMenu editor = new JMenu("Editor");
        JMenuItem editorMenu = new JMenuItem("Open editor");
        JMenuItem clearAngle = new JMenuItem("Clear rotation");
        editorMenu.addActionListener(editorAction);
        clearAngle.addActionListener(clearAngleAction);
        editor.add(editorMenu);
        editor.add(clearAngle);
        menuBar.add(editor);


        JMenuItem help = new JMenuItem("Help");
        help.addActionListener(helpAction);
        help.setMaximumSize(help.getPreferredSize());
        help.setBackground(Color.WHITE);

        menuBar.add(help);
        menuBar.setBorder(new MatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        return menuBar;
    }

    private Icon loadButtonIcon(String path) {
        try {
            URL url = MainFrame.class.getResource(path);
            if (url == null) {
                throw new RuntimeException("Can't found image " + path);
            }
            Image image = ImageIO.read(url).getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            return new ImageIcon(image);
        } catch (IOException e) {
            throw new RuntimeException("Image load error " + path, e);
        }
    }

    private void setFigureInfo(FigureInfoRecord figureInfo) {
        this.figureInfo = figureInfo;
        showFrame.setLinesSupplier(new FigureVertexContainer(figureInfo));
    }

}
