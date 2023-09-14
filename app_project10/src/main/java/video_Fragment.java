package com.example.project10;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class video_Fragment extends Fragment {

    public video_Fragment() {
        // Required empty public constructor
    }

    private List<Things> list_video = new ArrayList<Things>();
    ListView listview_video;
    VideoView videoView;
    JSONArray data_store;
    JSONArray data_read;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_, container, false);

        videoView = view.findViewById(R.id.videoView);
        listview_video = view.findViewById(R.id.listview_video);
        populateVideo();
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
        FileInputStream fis = getActivity().openFileInput("all_video");
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
            list_video.add(item);
        }
    }

    /**
     * initialize the three default songs
     */
    public void storeVideo() {
        Things video_first = new Things("cricket", false, 1, R.raw.cricket);
        Things video_second = new Things("football", false, 1, R.raw.football);
        Things video_third = new Things("tabletennis", false, 1, R.raw.tabletennis);
        list_video.add(video_first);
        list_video.add(video_second);
        list_video.add(video_third);
    }

    /**
     * populate songs
     */
    public void populateVideo() {
        try {
            readJSONFile();
        } catch (IOException e) {
            storeVideo();
        } catch (JSONException e) {
            storeVideo();
        }
    }

    /**
     * populate the custom listView we created
     */
    private void populateCustomListView() {
        ArrayAdapter<Things> myAdapter = new video_Fragment.MyCustomListAdapter();
        listview_video.setAdapter(myAdapter);
    }

    private class MyCustomListAdapter extends ArrayAdapter<Things> {
        /**
         * custom list adapter
         */
        public MyCustomListAdapter() {
            super(video_Fragment.this.getContext(), R.layout.item_layout, list_video);
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
            name.setText(list_video.get(position).getName());
            if (list_video.get(position).getFavorite()) {
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
                    switch (position) {
                        case 0:
                            String videolink_cricket = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.cricket;
                            Uri uri_cricket =  Uri.parse(videolink_cricket);
                            videoView.setVideoURI(uri_cricket);
                            break;
                        case 1:
                            String videolink_football = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.football;
                            Uri uri_football =  Uri.parse(videolink_football);
                            videoView.setVideoURI(uri_football);
                            break;
                        case 2:
                            String videolink_table = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.tabletennis;
                            Uri uri_table =  Uri.parse(videolink_table);
                            videoView.setVideoURI(uri_table);
                            break;
                    }

                    MediaController mediaController = new MediaController(getContext());
                    videoView.setMediaController(mediaController);
                    mediaController.setAnchorView(videoView);
                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                /**
                 * save the song to playlist if clicked
                 * @param view
                 */
                @Override
                public void onClick(View view) {
                    if (list_video.get(position).getFavorite()) {
                        button.setText("favorite");
                        list_video.get(position).setFavorite(false);
                    }
                    else {
                        button.setText("added_fav");
                        list_video.get(position).setFavorite(true);
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
            for (int i = 0; i < list_video.size(); i++) {
                JSONObject thing = new JSONObject();
                thing.put("name", list_video.get(i).getName());
                thing.put("favorite", list_video.get(i).getFavorite());
                thing.put("choice", list_video.get(i).getChoice());
                thing.put("link", list_video.get(i).getLink());
                data_store.put(thing);
                String text = data_store.toString();
                FileOutputStream fos = getActivity().openFileOutput("all_video", Context.MODE_PRIVATE);
                fos.write(text.getBytes());
                fos.close();
            }
        }
    }
}