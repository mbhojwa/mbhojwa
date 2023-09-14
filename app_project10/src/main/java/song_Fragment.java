package com.example.project10;

import android.content.Context;
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
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class song_Fragment extends Fragment {

    public song_Fragment() {
        // Required empty public constructor
    }

    private List<Things> list_song = new ArrayList<Things>();
    ListView listview_song;
    Intent intent;
    JSONArray data_store;
    JSONArray data_read;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_, container, false);

        intent = new Intent(getActivity(), service.class);
        listview_song = view.findViewById(R.id.listview_song);
        populateSongs();
        populateCustomListView();

        // Inflate the layout for this fragment
        return view;
    }

    /**
     * read the JSON file that contains the songs' info and add them to songs
     * @throws IOException
     * @throws JSONException
     */
    public void readJSONFile() throws IOException, JSONException {
        FileInputStream fis = getActivity().openFileInput("all_song");
        BufferedInputStream bis = new BufferedInputStream(fis);
        StringBuffer b = new StringBuffer();
        while (bis.available() != 0) {
            b.append((char) bis.read());
        }
        bis.close();
        fis.close();
        data_read = new JSONArray(b.toString());
        for (int i = 0; i < data_read.length(); i++) {
            Things item = new Things(data_read.getJSONObject(i).getString("name"),
                    data_read.getJSONObject(i).getBoolean("favorite"),
                    data_read.getJSONObject(i).getInt("choice"),
                    data_read.getJSONObject(i).getInt("link"));
            list_song.add(item);
        }
    }

    /**
     * initialize the three default songs
     */
    public void storeSongs() {
        Things song_first = new Things("chimes", false, 1, R.raw.chimes);
        Things song_second = new Things("impov", false, 1, R.raw.impov);
        Things song_third = new Things("night", false, 1, R.raw.night);
        list_song.add(song_first);
        list_song.add(song_second);
        list_song.add(song_third);
    }

    /**
     * populate songs
     */
    public void populateSongs() {
        try {
            readJSONFile();
        } catch (IOException e) {
            storeSongs();
        } catch (JSONException e) {
            storeSongs();
        }
    }

    /**
     * populate the custom listView we created
     */
    private void populateCustomListView() {
        ArrayAdapter<Things> myAdapter = new MyCustomListAdapter();
        listview_song.setAdapter(myAdapter);
    }

    private class MyCustomListAdapter extends ArrayAdapter<Things> {
        /**
         * custom list adapter
         */
        public MyCustomListAdapter() {
            super(song_Fragment.this.getContext(), R.layout.item_layout, list_song);
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
            name.setText(list_song.get(position).getName());
            if (list_song.get(position).getFavorite()) {
                button.setText("added_fav");
            }
            else {
                button.setText("favorite");
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                /**
                 * start the service to play the song when clicked
                 * @param view
                 */
                @Override
                public void onClick(View view) {
                    for (int i = position; i < list_song.size(); i++) {
                        intent.putExtra("song" + i, list_song.get(i).getLink());
                    }
                    intent.putExtra("song_position", position);
                    intent.putExtra("song_length", list_song.size());
                    getActivity().startService(intent);
                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                /**
                 * save the song to playlist if clicked
                 * @param view
                 */
                @Override
                public void onClick(View view) {
                    if (list_song.get(position).getFavorite()) {
                        button.setText("favorite");
                        list_song.get(position).setFavorite(false);
                    }
                    else {
                        button.setText("added_fav");
                        list_song.get(position).setFavorite(true);
                    }

                    try {
                        changeThings();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            return itemView;
        }

        /**
         * update the data in the JSON file
         * @throws JSONException
         * @throws IOException
         */
        public void changeThings() throws JSONException, IOException {
            data_store = new JSONArray();
            for (int i = 0; i < list_song.size(); i++) {
                JSONObject thing = new JSONObject();
                thing.put("name", list_song.get(i).getName());
                thing.put("favorite", list_song.get(i).getFavorite());
                thing.put("choice", list_song.get(i).getChoice());
                thing.put("link", list_song.get(i).getLink());
                data_store.put(thing);
                String text = data_store.toString();
                FileOutputStream fos = getActivity().openFileOutput("all_song", Context.MODE_PRIVATE);
                fos.write(text.getBytes());
                fos.close();
            }
        }
    }
}