package com.student.tool.MangaServer;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.student.tool.MangaServer.connect.Main_Base;
import com.student.tool.MangaServer.connect.Main_Base_interface;
import com.student.tool.MangaServer.connect.memory.Storage;

public class ThirdFragment extends Fragment implements Main_Base_interface {

    private static final String ACTIVITY_TAG = "Manga ThirdFragment";
    private Storage _storage = null;
    private Main_Base _engine = null;
    private MainActivity _gui_main = null;

    private int _size_height;
    private int _size_width;
    private int shift_plus_y;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        Log.d(ThirdFragment.ACTIVITY_TAG, "onCreate start");

        _gui_main = (MainActivity) getActivity();
        _storage = _gui_main.getStorage();
        _engine = _gui_main.getEngine();
        _engine.setModeInt(3);

        _engine.setThirdEngine(this);
        initial_touch_range((int) _gui_main.getPictureWidth(), (int) _gui_main.getPictureHeight());

        _size_height = (int) _gui_main.getPictureHeight();
        _size_width = (int) _gui_main.getPictureWidth();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_third, container, false);
    }

    private static int screen_height_mid = 0;
    private static int screen_width_mid = 0;
    private static int screen_height_three_one = 0;
    private static int screen_height_three_two = 0;

    private static void initial_touch_range(int width, int height)
    {
        screen_height_mid      = (int) (height / 2.0);
        screen_width_mid       = (int) ( width / 2.0);
        screen_height_three_one = (int) ( height / 3.0);
        screen_height_three_two = (int) ( height / 3.0 * 2);
    }

    private ImageView _image_01 = null;
    private TextView _label_01 = null;

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(ThirdFragment.ACTIVITY_TAG, "onViewCreated start");

        _image_01 = (ImageView) view.findViewById(R.id.third_image_01);
        _label_01 = (TextView)  view.findViewById(R.id.third_label_01);
        _label_01.setX(0);
        _label_01.setY(0);
        //_label_01.setX((float) (-_size_width*8.0/100.0));
        //_label_01.setY((float) (_size_height*5.0/100.0));
        shift_plus_y = (int) (_size_height*4.0/100.0);
        _image_01.setImageMatrix(new Matrix());


        view.findViewById(R.id.third_screen).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event){

                if(event.getAction() != MotionEvent.ACTION_DOWN) return true;

                int posi_x = (int) event.getX();
                int posi_y = (int) event.getY();

                if (posi_x <= screen_width_mid) {
                    // nextDualPage, prevDualPage
                    if (posi_y <= screen_height_mid) {
                        Log.d(ThirdFragment.ACTIVITY_TAG, "send_command nextPage");
                        _engine.send_command("nextPage");
                    } else {
                        Log.d(ThirdFragment.ACTIVITY_TAG, "send_command prevPage");
                        _engine.send_command("prevPage");
                    }
                } else {
                    // next page, single_page,prev page
                    if (posi_y <= screen_height_three_one) {
                        Log.d(ThirdFragment.ACTIVITY_TAG, "send_command nextChapter");
                        _engine.send_command("nextChapter");
                    } else if (posi_y <= screen_height_three_two) {
                        if(!_engine.have_send_command()) {
                            Log.d(ThirdFragment.ACTIVITY_TAG, "send_command switch_occur");
                            onBackPressed();
                        }
                    } else {
                        Log.d(ThirdFragment.ACTIVITY_TAG, "send_command prevChapter");
                        _engine.send_command("prevChapter");
                    }
                }
                return true;
            }
        });
        _handle = new Handler_Me(_label_01, _image_01, _storage, _size_width, _size_height, shift_plus_y);
        show_image_on_screen(_gui_main.getShowIndex(), _gui_main.getShowIsNext(), false);

        //Log.d(ThirdFragment.ACTIVITY_TAG, "onViewCreated finish");
        //NavHostFragment.findNavController(ThirdFragment.this)
        //       .navigate(R.id.action_SecondFragment_to_FirstFragment);
    }

    private boolean switch_occur = false;
    private Handler_Me  _handle = null;

    static class Runable_Me implements Runnable {

        ThirdFragment.Handler_Me _handle;

        Runable_Me(ThirdFragment.Handler_Me handle) { _handle = handle; }

        @Override
        public void run() {
            Message msg = new Message();
            msg.what = 1;
            _handle.sendMessage(msg);
        }
    }
    static class Handler_Me extends Handler {

        TextView _label;
        ImageView _image;
        Storage _storage;
        int _index = 0;
        String _text = null;
        int _size_width;
        int _size_height;
        int shift_plus_y;

        Handler_Me(TextView label, ImageView image, Storage storage, int size_width, int size_height, int plus_y) {
            _image = image;
            _storage = storage;
            _label = label;
            _size_width = size_width;
            _size_height = size_height;
            shift_plus_y = plus_y;
        }
        void setIndex(int index){ _index = index; }
        void setText(String str){ _text = str; }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1)
            {
                Bitmap bmp = _storage.get_memory(_index);
                if(bmp != null)
                {
                    _image.setImageBitmap(bmp);
                    _image.setX((float) ((_size_width-bmp.getWidth()) / 2.0));
                    if((_size_height-bmp.getHeight())/2.0 > shift_plus_y) {
                        _image.setY((float) ((_size_height - bmp.getHeight())/2.0));
                    } else {
                        _image.setY(_size_height - bmp.getHeight());
                    }
                }
                if(_text != null)
                    _label.setText(_text);
            }
        }
    }

    public synchronized void show_image_on_screen(int index, boolean is_next, boolean from_thread)
    {
        if (switch_occur) {
            Log.d(ThirdFragment.ACTIVITY_TAG, "show_image_on_screen switch_occur");
            switch_occur = false;
            _gui_main.setArgumentForShow(index, is_next);
            return;
        }
        Log.d(ThirdFragment.ACTIVITY_TAG, "show_image_on_screen");
        if (from_thread) {
            Log.d(ThirdFragment.ACTIVITY_TAG, "show_image_on_screen from non gui thread");
            _handle.setIndex(index);
            _handle.setText(_engine.getLabelString());
            Thread t = new Thread(new ThirdFragment.Runable_Me(_handle));
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                Log.d(ThirdFragment.ACTIVITY_TAG, "show_image_on_screen error\n" + e);
                _gui_main._close_program();
            }
        } else {
            Log.d(ThirdFragment.ACTIVITY_TAG, "show_image_on_screen from gui");
            Bitmap bmp = _storage.get_memory(index);
            if(bmp != null)
            {
                _image_01.setImageBitmap(bmp);
                _label_01.setText(_engine.getLabelString());
                _image_01.setX((float) ((_size_width-bmp.getWidth()) / 2.0));
                if((_size_height-bmp.getHeight())/2.0 > shift_plus_y) {
                    _image_01.setY((float) ((_size_height - bmp.getHeight())/2.0));
                } else {
                    _image_01.setY(_size_height - bmp.getHeight());
                }
            }
        }
    }

    public void onBackPressed()
    {
        switch_occur = true;
        _engine.switch_page_show_mode_fragment_third(_storage, false);
        _engine.setModeInt(1);
        NavHostFragment.findNavController(ThirdFragment.this)
                .navigate(R.id.action_ThirdFragment_to_FirstFragment);
    }

    public void _close_program(){}
    public void send_command_fail()
    {
        Log.d(ThirdFragment.ACTIVITY_TAG, "send_command_fail");
    }
}