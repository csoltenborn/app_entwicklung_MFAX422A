package de.fhdw.app_entwicklung.chatgpt.model;

import de.fhdw.app_entwicklung.chatgpt.R;

public enum Author {
    User(R.string.user), Assistant(R.string.assistant), System(R.string.system);

    public final int ID;
    Author(final int ID){
       this.ID = ID;
    }
}