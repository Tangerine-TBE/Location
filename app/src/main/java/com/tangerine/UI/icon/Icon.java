package com.tangerine.UI.icon;

public enum  Icon  implements com.joanzapata.iconify.Icon {
    lo_more('\ue63d'),
    ;
    char aChar;
    Icon(char aChar){
        this.aChar = aChar;
    }
    @Override
    public String key() {
        return name().replace('_', '-');
    }

    @Override
    public char character() {
        return aChar;
    }
}
