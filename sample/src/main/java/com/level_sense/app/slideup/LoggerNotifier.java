package com.level_sense.app.slideup;

/**
 * @author YogeshT (05.07.2017)
 */
interface LoggerNotifier {
    
    void notifyPercentChanged(float percent);
    
    void notifyVisibilityChanged(int visibility);
}
