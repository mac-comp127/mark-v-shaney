package mvs;

import java.util.ArrayList;
import java.util.List;

class ChainWalker {
    private final MarkVShaney mvs;
    private final List<String> context = new ArrayList<>();

    ChainWalker(MarkVShaney mvs) {
        this.mvs = mvs;
    }

    private void shiftIntoContext(String word) {
        if (context.size() >= mvs.getContextSize()) {
            context.remove(0);
        }
        context.add(word);
    }

    void addNext(String word) {
        mvs.addChoice(context, word);
        shiftIntoContext(word);
    }

    String chooseNext() {
        String result = mvs.chooseNextWord(context);
        shiftIntoContext(result);
        return result;
    }
}
