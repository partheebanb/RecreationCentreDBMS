package ca.ubc.cs304.ui;

/*
 * THIS IS FOR UI PURPOSES ONLY THERE IS NO EXTRA FUNCTIONALITIES FOR THIS FEATURE AT ALL
 * 
 * Disposable Window is a system implemented to keep the windows from being to cluttered
 * It closes all the childrens whenever the parent window is clicked so there won't be too many
 * windows running around
 */
public interface DisposableWindow {
    void close();
}
