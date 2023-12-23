package com.example.workTogetherTimeTracker.parser;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

public interface Parser<T> {

    Set<T> parse(InputStream inputStream);
}
