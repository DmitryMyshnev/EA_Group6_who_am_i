package com.eleks.academy.whoami.core;

import com.eleks.academy.whoami.core.impl.AnsweringPlayer;
import com.eleks.academy.whoami.core.impl.Entry;

import java.util.List;

public interface History {

    void addNewEntry(Entry entry);

    void addAnswerToEntry(AnsweringPlayer player);

    List<Entry> getEntries();
}
