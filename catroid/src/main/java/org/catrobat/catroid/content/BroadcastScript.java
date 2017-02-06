/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2016 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.content;

import android.content.Context;

import org.catrobat.catroid.common.Constants;
import org.catrobat.catroid.common.MessageContainer;
import org.catrobat.catroid.content.bricks.BroadcastReceiverBrick;
import org.catrobat.catroid.content.bricks.ScriptBrick;
import org.catrobat.catroid.utils.Utils;

public class BroadcastScript extends Script implements BroadcastMessage, Translatable {

	private static final long serialVersionUID = 1L;
	protected String receivedMessage;

	public BroadcastScript(String broadcastMessage) {
		super();
		setBroadcastMessage(broadcastMessage);
	}

	@Override
	public ScriptBrick getScriptBrick() {
		if (brick == null) {
			brick = new BroadcastReceiverBrick(this);
		}

		return brick;
	}

	@Override
	protected Object readResolve() {
		MessageContainer.addMessage(receivedMessage, this);
		super.readResolve();
		return this;
	}

	@Override
	public String getBroadcastMessage() {
		return receivedMessage;
	}

	@Override
	public void setMessage(String broadcastMessage) {
		setBroadcastMessage(broadcastMessage);
	}

	public void setBroadcastMessage(String broadcastMessage) {
		MessageContainer.removeReceiverScript(this.receivedMessage, this);
		this.receivedMessage = broadcastMessage;
		MessageContainer.addMessage(this.receivedMessage, this);
	}

	@Override
	public Script copyScriptForSprite(Sprite copySprite) {
		BroadcastScript cloneScript = new BroadcastScript(receivedMessage);

		doCopy(copySprite, cloneScript);
		return cloneScript;
	}

	@Override
	public String translate(String templateName, Scene scene, Sprite sprite, Context context) {
		String key = templateName + Constants.TRANSLATION_BROADCAST_MESSAGE;
		String value = getBroadcastMessage();

		setMessage(Utils.getStringResourceByName(Utils.getStringResourceName(key, value), value, context));
		return Utils.createStringEntry(key, value);
	}
}
