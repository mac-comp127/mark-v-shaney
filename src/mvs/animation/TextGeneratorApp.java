package mvs.animation;

import java.awt.Color;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsObject;
import edu.macalester.graphics.GraphicsText;
import mvs.MarkVShaney;

public class TextGeneratorApp {
    private static final int
        WINDOW_WIDTH = 800,
        WINDOW_HEIGHT = 2000;
    private static final double
        MARGIN = 10,
        FONT_SIZE = 20,
        SPACE_WIDTH = 6;
    private static final double
        NORMAL_WORD_RATE = 0.15,
        FAST_WORD_RATE = 0.01;

    private Iterator<String> wordGenerator;

    private final CanvasWindow canvas;
    private List<FlyUpAnimation> wordAnimations = new LinkedList<>();
    private double nextWordX, nextWordY;
    private double timeUntilNextWord;
    private double lineHeight;
    private Color textColor;

    public TextGeneratorApp() {
        MarkVShaney mvs = new MarkVShaney(2, true);
        mvs.readDefaultCorpus();
        wordGenerator = mvs.generateText().iterator();

        canvas = new CanvasWindow("Mark V. Shaney", WINDOW_WIDTH, WINDOW_HEIGHT);
        canvas.setBackground(Color.BLACK);

        canvas.animate(this::generateWords);
        canvas.animate(this::scroll);
        canvas.animate(dt -> {
            for (FlyUpAnimation animation : wordAnimations) {
                animation.animate(dt);
            }
        });

        nextWordY = canvas.getHeight() / 2;
        newLine();
        randomizeTextColor();
    }

    private void generateWords(double dt) {
        timeUntilNextWord -= dt;
        while (timeUntilNextWord < 0 && wordGenerator.hasNext()) {
            timeUntilNextWord += canvas.getKeysPressed().isEmpty() ? NORMAL_WORD_RATE : FAST_WORD_RATE;
            showWord(wordGenerator.next());
        }
    }

    private void showWord(String word) {
        GraphicsText wordGraphics = new GraphicsText(word.trim());
        wordGraphics.setFontSize(FONT_SIZE);
        wordGraphics.setFillColor(textColor);
        wordGraphics.setY(canvas.getHeight());
        wordGraphics.setRotation(Math.pow(Math.random(), 10) * 1000);
        FlyUpAnimation animation = new FlyUpAnimation(
            wordGraphics,
            Math.min(0.5, 0.004 * Math.pow(word.length(), 1.5)),  // longer words float up more slowly
            5);
        canvas.add(animation);
        wordAnimations.add(animation);

        lineHeight = wordGraphics.getLineHeight();
        if (nextWordX + wordGraphics.getAdvance() > canvas.getWidth() - MARGIN) {
            newLine();
        }
        animation.setPosition(nextWordX, nextWordY);
        nextWordX += wordGraphics.getAdvance() + SPACE_WIDTH;

        if (word.contains("\n")) {
            newLine();
            randomizeTextColor();
            nextWordX += MARGIN * 3;
        }
    }

    private void newLine() {
        nextWordX = MARGIN;
        nextWordY += lineHeight;
    }

    private void randomizeTextColor() {
        textColor = Color.getHSBColor((float) Math.random(), (float) Math.random(), 1);
    }

    private void scroll(double dt) {
        double progress = (nextWordY + nextWordX / canvas.getWidth() * lineHeight) / canvas.getHeight();
        double dy = -dt * Math.max(0, Math.pow(2, progress - 0.7) - 1) * canvas.getHeight();
        nextWordY += dy;
        for (GraphicsObject word : wordAnimations) {
            word.moveBy(0, dy);
            if (word.getBoundsInParent().getMaxY() < 0) {
                canvas.remove(word);
            }
        }
        wordAnimations.removeIf(word -> word.getCanvas() == null);
    }

    public static void main(String[] args) {
        new TextGeneratorApp();
    }
}
