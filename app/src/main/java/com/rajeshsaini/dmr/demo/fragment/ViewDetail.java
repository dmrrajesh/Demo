package com.rajeshsaini.dmr.demo.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rajeshsaini.dmr.demo.Admin;
import com.rajeshsaini.dmr.demo.R;
import com.rajeshsaini.dmr.demo.fragment.inner.CallLogs;
import com.rajeshsaini.dmr.demo.fragment.inner.Location;
import com.rajeshsaini.dmr.demo.fragment.inner.Message;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewDetail.OnViewDetailListener} interface
 * to handle interaction events.
 * Use the {@link ViewDetail#newInstance} factory method to
 * create an instance of this fragment.
 */

public class ViewDetail extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String TAG= ViewDetail.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnViewDetailListener mListener;
    private SectionPagerAdapter sectionPagerAdapter;
    private ViewPager viewPager;

    private String child_id;

    public ViewDetail() {
        // Required empty public constructor
    }

    public static ViewDetail newInstance(){
        return new ViewDetail();
    }
    public static ViewDetail newInstance(String child_id){
        ViewDetail fragment = new ViewDetail();
        Bundle args = new Bundle();
        args.putString(Admin.CHILD_ID, child_id);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewDetail.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewDetail newInstance(String param1, String param2) {
        ViewDetail fragment = new ViewDetail();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
            child_id=getArguments().getString(Admin.CHILD_ID);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_view_detail,container,false);
        //child_id = getActivity().getIntent().getExtras().getString(Admin.CHILD_ID);
        sectionPagerAdapter=new SectionPagerAdapter(getChildFragmentManager());
        viewPager=(ViewPager)view.findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionPagerAdapter);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onViewDetail(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnViewDetailListener) {
            mListener = (OnViewDetailListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnViewDetailListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnViewDetailListener {
        // TODO: Update argument type and name
        void onViewDetail(Uri uri);
    }
    public class SectionPagerAdapter extends FragmentPagerAdapter{
        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new Location();
                case 1:
                    return new CallLogs();
            }
            return null;
        /*
            Fragment fragment = new DummySectionFragment();
            Bundle args = new Bundle();
            args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
            fragment.setArguments(args);
            return fragment;
            */
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
            }
            return super.getPageTitle(position);
        }
    }
    public static class DummySectionFragment extends Fragment{
        public static final String ARG_SECTION_NUMBER = "section_number";
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view=inflater.inflate(R.layout.fragment_dummy,container,false);
            TextView textView=(TextView)view.findViewById(R.id.selection_level);
            textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            return view;
        }
    }
}
