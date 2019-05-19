package com.techexchange.mobileapps.recyclerdemo;

final class Contact {
    final String name;
    final String number;

    Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    @Override
    public String toString() {
        return "[" + name + ", " + number + "]";
    }
}