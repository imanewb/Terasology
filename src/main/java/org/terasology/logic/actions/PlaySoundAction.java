/*
 * Copyright 2013 Moving Blocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.logic.actions;

import org.terasology.audio.AudioManager;
import org.terasology.audio.Sound;
import org.terasology.logic.actions.PlaySoundActionComponent;
import org.terasology.entitySystem.ComponentSystem;
import org.terasology.entitySystem.EntityRef;
import org.terasology.entitySystem.In;
import org.terasology.entitySystem.ReceiveEvent;
import org.terasology.entitySystem.RegisterMode;
import org.terasology.entitySystem.RegisterSystem;
import org.terasology.events.ActivateEvent;
import org.terasology.utilities.procedural.FastRandom;

import javax.vecmath.Vector3f;

/**
 * @author Immortius <immortius@gmail.com>
 */
@RegisterSystem(RegisterMode.ALWAYS)
public class PlaySoundAction implements ComponentSystem {

    private FastRandom random = new FastRandom();

    @In
    private AudioManager audioManager;

    public void initialise() {
    }

    @Override
    public void shutdown() {
    }

    @ReceiveEvent(components = {PlaySoundActionComponent.class})
    public void onActivate(ActivateEvent event, EntityRef entity) {
        PlaySoundActionComponent playSound = entity.getComponent(PlaySoundActionComponent.class);
        if (playSound.sounds.size() > 0) {
            Sound sound = playSound.sounds.get(random.randomIntAbs(playSound.sounds.size()));
            Vector3f pos = null;
            switch (playSound.relativeTo) {
                case Instigator:
                    pos = event.getInstigatorLocation();
                    break;
                case Target:
                    pos = event.getTargetLocation();
                    break;
            }
            if (pos == null) {
                pos = event.getOrigin();
            }
            audioManager.playSound(sound, pos, playSound.volume, AudioManager.PRIORITY_NORMAL);
        }
    }
}