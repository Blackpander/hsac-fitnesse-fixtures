package nl.hsac.fitnesse.fixture.slim;

import com.sksamuel.diffpatch.DiffMatchPatch;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.LinkedList;

/**
 * Fixture to determine and visualize differences between strings.
 */
public class CompareFixture {
    private final DiffMatchPatch diffMatchPatch = new DiffMatchPatch();

    public String diffToHtml(LinkedList<DiffMatchPatch.Diff> diffs) {
        StringBuilder html = new StringBuilder();
        if (diffs.size() == 1 && diffs.get(0).operation == DiffMatchPatch.Operation.EQUAL) {
            html.append(StringEscapeUtils.escapeHtml4(diffs.get(0).text));
        } else {
            for (DiffMatchPatch.Diff aDiff : diffs) {
                String text = StringEscapeUtils.escapeHtml4(aDiff.text);
                switch (aDiff.operation) {
                    case INSERT:
                        text = replaceSpaceAtStartAndEnd(text);
                        html.append("<ins class=\"collapse_rim\">").append(text)
                                .append("</ins>");
                        break;
                    case DELETE:
                        text = replaceSpaceAtStartAndEnd(text);
                        html.append("<del class=\"collapse_rim\">").append(text)
                                .append("</del>");
                        break;
                    case EQUAL:
                        html.append("<span>").append(text).append("</span>");
                        break;
                }
            }
        }
        return html.toString();
    }

    protected String replaceSpaceAtStartAndEnd(String text) {
        return text.replaceFirst("^ ", "&nbsp;")
                .replaceFirst(" $", "&nbsp;");
    }

    /**
     * Determines difference between two strings.
     * @param first first string to compare.
     * @param second second string to compare.
     * @return HTML difference between the two.
     */
    public String differenceBetweenAnd(String first, String second) {
        if (first == null) {
            if (second == null) {
                return null;
            } else {
                first = "";
            }
        } else if (second == null) {
            second = "";
        }
        LinkedList<DiffMatchPatch.Diff> diffs = diffMatchPatch.diff_main(first, second);
        diffMatchPatch.diff_cleanupSemantic(diffs);
        String diffPrettyHtml = diffToHtml(diffs);
        return "<div>" + diffPrettyHtml + "</div>";
    }
}
