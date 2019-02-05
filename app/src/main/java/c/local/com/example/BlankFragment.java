package c.local.com.example;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BlankFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private OnFragmentInteractionListener mListener;

	public BlankFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment BlankFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static BlankFragment newInstance(String param1, String param2) {
		BlankFragment fragment = new BlankFragment();
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
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_blank, container, false);

		// テキストビュー ※ line-height的な「android:lineSpacingMultiplier="2"」を設定してるTextView
		TextView textView = view.findViewById(R.id.textView);
		// テキストビューの文言
		String str = getActivity().getResources().getString(R.string.hello_blank_fragment);
		// スパンのビルダー   ※ １つスペースを設ける、ここに画像を追加する予定
		SpannableStringBuilder ssb = new SpannableStringBuilder(" " + str);
		// 画像のサイズ 15dp
		int size = getActivity().getResources().getDimensionPixelSize(R.dimen.icon_size);
		// 禁煙マークのアイコン
		Drawable drawable = getActivity().getDrawable(R.drawable.icon_nosmoking);
		// 禁煙マークのアイコンを15dp x 15dpにする
		drawable.setBounds(0, 0, size, size);
		// ImageSpanに設定する
		ImageSpan imageSpan = new CustomImageSpan(drawable);
		// スパンに追加
		ssb.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		// TextViewに設定
		textView.setText(ssb);

		return view;
	}


	// ImageSpan imageSpan = new CustomImageSpan(drawable);

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnFragmentInteractionListener) {
			mListener = (OnFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnFragmentInteractionListener");
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
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		void onFragmentInteraction(Uri uri);
	}
}
