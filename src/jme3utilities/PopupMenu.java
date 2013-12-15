/*
 Copyright (c) 2013, Stephen Gold
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright
 notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 notice, this list of conditions and the following disclaimer in the
 documentation and/or other materials provided with the distribution.
 * Stephen Gold's name may not be used to endorse or promote products
 derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL STEPHEN GOLD BE LIABLE FOR ANY
 DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jme3utilities;

import com.google.common.base.Joiner;
import de.lessvoid.nifty.controls.MenuItemActivatedEvent;
import java.util.logging.Logger;
import org.bushe.swing.event.EventTopicSubscriber;

/**
 * Event subscriber for a simple Nifty popup menu.
 *
 * @author Stephen Gold <sgold@sonic.net>
 */
public class PopupMenu
        implements EventTopicSubscriber<MenuItemActivatedEvent<String>> {
    // *************************************************************************
    // constants

    /**
     * joiner for constructing action strings
     */
    final private static Joiner actionJoiner = Joiner.on(" ");
    /**
     * message logger for this class
     */
    final private static Logger logger =
            Logger.getLogger(PopupMenu.class.getName());
    // *************************************************************************
    // fields
    /**
     * which screen controls this popup menu: set by constructor
     */
    final private SimpleScreenController controller;
    /**
     * prefix words for the menu's action strings: set by constructor
     */
    final private String[] actionPrefixWords;
    // *************************************************************************
    // constructors

    /**
     * Instantiate for a particular screen controller, action prefix, and
     * element.
     *
     * @param controller which screen owns the popup menu (not null)
     * @param actionPrefixWords prefix words for action strings (unaffected, not
     * null)
     * @param elementId id of the popup element (not null)
     */
    PopupMenu(SimpleScreenController controller, String[] actionPrefixWords) {
        assert controller != null;
        assert actionPrefixWords != null;

        this.controller = controller;
        this.actionPrefixWords = actionPrefixWords.clone();
    }
    // *************************************************************************
    // EventTopicSubscriber methods

    /**
     * Handle an event from the Nifty GUI.
     *
     * @param ignored
     * @param event (not null)
     */
    @Override
    public void onEvent(String ignored, MenuItemActivatedEvent<String> event) {
        String itemName = event.getItem();
        /*
         * Generate the action string for the item by appending the item's
         * name to the menu's action prefix.
         */
        String actionString;
        int wordCount = actionPrefixWords.length;
        if (wordCount > 0) {
            String actionPrefix = actionJoiner.join(actionPrefixWords);
            actionString = actionJoiner.join(actionPrefix, itemName);
        } else {
            actionString = itemName;
        }
        /*
         * Perform the action and then close the popup.
         */
        SimpleScreenController.perform(actionString);
        controller.closeActivePopup();
    }
}