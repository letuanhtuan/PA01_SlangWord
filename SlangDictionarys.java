import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class Words {
    public String wordname;
    public String meaning;

    Words(String wordname, String meaning) {
        this.wordname = wordname;
        this.meaning = meaning;
    }
}

public class SlangDictionarys{
    private Map<String, Words> slangWords = new HashMap<>();
    private List<String> searchHistory = new ArrayList<>();

    public void loadSlang() throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader("slang.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parsedLine = line.split("`");
                String wordName = parsedLine[0];
                String meaning = parsedLine[1];
                Words words = new Words(wordName, meaning);
                slangWords.put(wordName, words);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveSlang() {
        try (FileWriter writer = new FileWriter("slang.txt")) {
            for (Map.Entry<String, Words> entry : slangWords.entrySet()) {
                String wordName = entry.getKey();
                String meaning = entry.getValue().meaning;
                writer.write(wordName + "`" + meaning + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Xử lý lỗi khi ghi file.
        }
    }

    public void searchAndUpdateHistory(String keyword, JTextArea resultArea) {
        searchHistory.add(keyword);
        if (slangWords.containsKey(keyword)) {
            resultArea.setText(slangWords.get(keyword).meaning);
        } else {
            resultArea.setText("Slang word not found.");
        }
    }

    public static void main(String[] args) {
        SlangDictionarys dictionary = new SlangDictionarys();
        try {
            dictionary.loadSlang();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Slang Dictionary");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        // Tạo một JTextArea để hiển thị lịch sử tìm kiếm
        JTextArea historyArea = new JTextArea(5, 30);
        historyArea.setEditable(false);

        // Tạo một JPanel để chứa nút "Search" và ô nhập liệu
        JPanel panel = new JPanel(new FlowLayout());
        frame.add(panel);

        JButton searchButton = new JButton("Search");
        JTextField searchField = new JTextField(20);
        JTextArea resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);

        panel.add(searchField);
        panel.add(searchButton);

        panel.add(resultArea);
        panel.add(historyArea); // Thêm lịch sử vào giao diện

        // ... Cài đặt ActionListener cho nút "Search" để hiển thị kết quả tìm kiếm
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = searchField.getText();
                dictionary.searchHistory.add(keyword);
                updateHistoryArea(historyArea, dictionary.searchHistory);

                if (dictionary.slangWords.containsKey(keyword)) {
                    resultArea.setText(dictionary.slangWords.get(keyword).meaning);
                } else {
                    resultArea.setText("Slang word not found.");
                }
            }
        });

        frame.setVisible(true);
    }

    private static void updateHistoryArea(JTextArea historyArea, List<String> history) {
        StringBuilder historyText = new StringBuilder("Search History:\n");
        for (String item : history) {
            historyText.append(item).append("\n");
        }
        historyArea.setText(historyText.toString());
    }
}

