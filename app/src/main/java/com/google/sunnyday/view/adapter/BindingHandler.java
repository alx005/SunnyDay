package com.google.sunnyday.view.adapter;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.sunnyday.utils.Utils;

public class BindingHandler {


    public void checkBtn(MaterialButtonToggleGroup group, int checkedId, boolean isChecked){
        MaterialButton selectedBtn = group.findViewById(checkedId);

//
//        if (isChecked) {
//            if (selectedBtn == binding.lightTheme) {
//                switchSelectedBtn(selectedBtn, binding.darkTheme);
//                Utils.saveIntToPref(R.string.theme, 1, getActivity());
//                getActivity().recreate();
//            } else {
//                switchSelectedBtn(selectedBtn, binding.lightTheme);
//                Utils.saveIntToPref(R.string.theme, 0, getActivity());
//                getActivity().recreate();
//            }
//        } else {
//            selectedBtn.setChecked(true);
//        }
    }

//    binding.theme.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
//        @Override
//        public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
//            MaterialButton selectedBtn = getActivity().findViewById(checkedId);
//
//            if (isChecked) {
//                if (selectedBtn == binding.lightTheme) {
//                    switchSelectedBtn(selectedBtn, binding.darkTheme);
//                    Utils.saveIntToPref(R.string.theme, 1, getActivity());
//                    getActivity().recreate();
//                } else {
//                    switchSelectedBtn(selectedBtn, binding.lightTheme);
//                    Utils.saveIntToPref(R.string.theme, 0, getActivity());
//                    getActivity().recreate();
//                }
//            } else {
//                selectedBtn.setChecked(true);
//            }
//        }
//    });

}
