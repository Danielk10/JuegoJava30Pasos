/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.diamon.nucleo;

import android.graphics.Bitmap;

public class Animacion {

	public static final int REPETIR = 0;

	public static final int NORMAL = 1;

	final Bitmap[] keyFrames;

	final float frameDuration;

	public Animacion(float frameDuration, Bitmap... keyFrames) {
		this.frameDuration = frameDuration;
		this.keyFrames = keyFrames;
	}

	public Bitmap getKeyFrame(float stateTime, int mode) {

		int frameNumber = (int) (stateTime / frameDuration);

		if (mode == NORMAL) {
			frameNumber = Math.min(keyFrames.length - 1, frameNumber);
		} else {
			frameNumber = frameNumber % keyFrames.length;
		}
		return keyFrames[frameNumber];
	}
}
