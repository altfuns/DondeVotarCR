package cr.quarks.dondevotarcr;

/**
 * Created by luisa on 2/5/16.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cr.quarks.dondevotarcr.model.Center;
import cr.quarks.dondevotarcr.model.Person;

/**
 * A vote fragment containing the search field.
 */
public class VoteFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private EditText editText;
    private LinearLayout results;
    private TextView textView;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static VoteFragment newInstance(int sectionNumber) {
        VoteFragment fragment = new VoteFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public VoteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        editText = (EditText) rootView.findViewById(R.id.editText);
        results = (LinearLayout) rootView.findViewById(R.id.results);
        textView = (TextView) rootView.findViewById(R.id.textView);
        textView2 = (TextView) rootView.findViewById(R.id.textView2);
        textView3 = (TextView) rootView.findViewById(R.id.textView3);
        textView4 = (TextView) rootView.findViewById(R.id.textView4);
        textView5 = (TextView) rootView.findViewById(R.id.textView5);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Person person = Person.findByIdentification(editText.getText().toString());
                if(person != null) {
                    Center center = Center.findByJunta(person.getJuntaId());
                    textView.setText(person.toString());
                    textView2.setText(String.valueOf(person.getJuntaId()));
                    textView3.setText(center.getName());
                    textView4.setText(center.getElectoralDistrict());
                    textView5.setText(center.getCanton());
                    results.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(VoteFragment.this.getContext(), "No se encontró el elector. Puede que vote en otro centro de votación.", Toast.LENGTH_LONG).show();
                }

                editText.selectAll();

                return true;
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
