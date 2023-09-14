package com.example.project10;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class playlist_Fragment extends Fragment {

    public playlist_Fragment() {
        // Required empty public constructor
    }

    static List<Things> list_playList = new ArrayList<Things>();
    ListView listView_playlist;
    JSONArray data;
    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist_, container, false);

        intent = new Intent(getActivity(), service.class);
        listView_playlist = view.findViewById(R.id.listview_playlist);
        try {
            list_playList.clear();
            populatePlayList();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        populateCustomListView();

        // Inflate the layout for this fragment
        return view;
    }

    /**
     * populate the playList by getting the data from JSON file
     * @throws IOException
     * @throws JSONException
     */
    public void populatePlayList() throws IOException, JSONException {
        FileInputStream fis1 = getActivity().openFileInput("all_video");
        BufferedInputStream bis1 = new BufferedInputStream(fis1);
        StringBuffer b1 = new StringBuffer();
        while (bis1.available() != 0) {
            b1.append((char) bis1.read());
        }
        bis1.close();
        fis1.close();

        FileInputStream fis2 = getActivity().openFileInput("all_song");
        BufferedInputStream bis2 = new BufferedInputStream(fis2);
        StringBuffer b2 = new StringBuffer();
        while (bis2.available() != 0) {
            b2.append((char) bis2.read());
        }
        bis2.close();
        fis2.close();

        data = new JSONArray(b1.toString());
        for (int i = 0; i < data.length(); i++) {
            if (data.getJSONObject(i).getBoolean("favorite")) {
                Things thing = new Things(data.getJSONObject(i).getString("name"),
                        data.getJSONObject(i).getBoolean("favorite"),
                        data.getJSONObject(i).getInt("choice"),
                        data.getJSONObject(i).getInt("link"));
                list_playList.add(thing);
            }
        }

        data = new JSONArray(b2.toString());
        for (int i = 0; i < data.length(); i++) {
            if (data.getJSONObject(i).getBoolean("favorite")) {
                Things thing = new Things(data.getJSONObject(i).getString("name"),
                        data.getJSONObject(i).getBoolean("favorite"),
                        data.getJSONObject(i).getInt("choice"),
                        data.getJSONObject(i).getInt("link"));
                list_playList.add(thing);
            }
        }
    }

    /**
     * populate custom listView
     */
    private void populateCustomListView() {
        ArrayAdapter<Things> myAdapter = new MyCustomListAdapter();
        listView_playlist.setAdapter(myAdapter);
    }

    private class MyCustomListAdapter extends ArrayAdapter<Things> {
        /**
         * custom list adapter
         */
        public MyCustomListAdapter() {
            super(playlist_Fragment.this.getContext(), R.layout.item_layout, list_playList);
        }

        /**
         * override function to get the view
         * @param position
         * @param convertView
         * @param parent
         * @return
         */
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.item_layout, parent, false);
            TextView name = itemView.findViewById(R.id.textviewitem_name);
            Button button = itemView.findViewById(R.id.button_favorite);
            name.setText(list_playList.get(position).getName());
            button.setText("added_fav");


            itemView.setOnClickListener(new View.OnClickListener() {
                /**
                 * start the service to play the song when clicked
                 * @param view
                 */
                @Override
                public void onClick(View view) {
                    BottomSheetClass.Playing.setText(list_playList.get(position).getName());
                    for (int i = position; i < list_playList.size(); i++) {
                        intent.putExtra("song" + i, list_playList.get(i).getLink());
                    }
                    intent.putExtra("song_position", position);
                    intent.putExtra("song_length", list_playList.size());
                    getActivity().startService(intent);
                }
            });

            return itemView;
        }
    }
}