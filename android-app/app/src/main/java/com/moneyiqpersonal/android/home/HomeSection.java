package com.moneyiqpersonal.android.home;

import com.moneyiqpersonal.android.R;

public enum HomeSection {
    DASHBOARD(R.string.tab_dashboard, R.string.dashboard_description),
    RECEITAS(R.string.tab_receitas, R.string.receitas_description),
    DESPESAS(R.string.tab_despesas, R.string.despesas_description),
    CONTA(R.string.tab_conta, R.string.conta_description);

    private final int titleRes;
    private final int descriptionRes;

    HomeSection(int titleRes, int descriptionRes) {
        this.titleRes = titleRes;
        this.descriptionRes = descriptionRes;
    }

    public int getTitleRes() {
        return titleRes;
    }

    public int getDescriptionRes() {
        return descriptionRes;
    }
}
