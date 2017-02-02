package com.example.probearbeit.treeapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    private static TextView textLog;

    private EditText editSearch;
    private TreeNode tree;
    private Switch switchSearch;
    private ScrollView scroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textLog = (TextView) findViewById(R.id.textLog);
        editSearch = (EditText) findViewById(R.id.editSearch);
        switchSearch = (Switch) findViewById(R.id.switchSearch);
        scroller = (ScrollView) findViewById(R.id.scroller);

        findViewById(R.id.btnGo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchSearch.isChecked()) {
                    tree.setTraverse(TreeNode.Traverse.DEPTH_FIRST);
                    log("------------------- starting depth-first");
                } else {
                    tree.setTraverse(TreeNode.Traverse.BREADTH_FIRST);
                    log("------------------- starting breadth-first");
                }

                boolean found = tree.find(editSearch.getText().toString());
                log("------------------- found: " + found);
                scroller.post(new Runnable() {
                    @Override
                    public void run() {
                        scroller.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textLog.setText("");
            }
        });

        try {
            String json = "";
            BufferedReader bufRead = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.cities)));
            String line = bufRead.readLine();
            while (line != null) {
                json += line;
                line = bufRead.readLine();
            }

            JSONObject obj = new JSONObject(json);
            tree = new TreeNode(obj.getJSONObject("root"), null, TreeNode.Traverse.DEPTH_FIRST);
            log("tree loaded");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void log(String msg) {
        if (textLog.getText().length() == 0) {
            textLog.append("" + msg);
        } else {
            textLog.append("\n" + msg);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
