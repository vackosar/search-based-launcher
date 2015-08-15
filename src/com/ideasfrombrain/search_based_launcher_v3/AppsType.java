package com.ideasfrombrain.search_based_launcher_v3;

public enum AppsType {
    normal(R.id.normalAppsRadioButton),
    activity(R.id.activityAppsRadioButton),
    extra(R.id.extraAppsRadioButton),
    hidden(R.id.hiddenAppsRadioButton);

    private final int radioButton;

    AppsType(int radioButton) {
        this.radioButton = radioButton;
    }

    public static AppsType of(int viewId) {
        for (AppsType appsType: AppsType.values()) {
            if (viewId == appsType.radioButton) {
                return appsType;
            }
        }
        throw new IllegalArgumentException("View id " + viewId + " could not be parsed.");
    }
}
