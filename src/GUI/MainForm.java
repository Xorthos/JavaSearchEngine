package GUI;

import BE.Document;
import DAL.DatabaseGateway;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.List;

public class MainForm {
    private JPanel PanelMain;
    private JTextField txtSearchFirld;
    private JButton btnSearch;
    private JList lstResult;
    private DefaultListModel<String> listModel = new DefaultListModel<>();

    public static void main(String[] args) {
        JFrame frame = new JFrame("SearchEngineV2");
        frame.setContentPane(new MainForm().PanelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        frame.setVisible(true);
    }

    public MainForm() {
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doSearch();
            }
        });
    }

    private void doSearch() {
        String[] terms = txtSearchFirld.getText().toLowerCase().split(" ");
        DatabaseGateway.getInstance().getDocuments(this, terms);
    }

    public void handleResult(HashSet<String> result){
        System.out.println("SIZE!!!!       " + result.size());
        listModel.clear();
        result.forEach(s -> listModel.addElement(s));

        lstResult.setModel(listModel);
    }
}
