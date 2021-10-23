package net.spookysquad.spookster.mod.mods;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import net.spookysquad.spookster.event.Event;
import net.spookysquad.spookster.event.events.EventPostHudRender;
import net.spookysquad.spookster.event.events.EventPreMotion;
import net.spookysquad.spookster.mod.HasValues;
import net.spookysquad.spookster.mod.Module;
import net.spookysquad.spookster.mod.Type;
import net.spookysquad.spookster.mod.values.Value;
import net.spookysquad.spookster.render.GuiUtil;
import net.spookysquad.spookster.utils.TimeUtil;

public class Notifications extends Module implements HasValues {

	public static CopyOnWriteArrayList<Entry<String, Long>> notifications = new CopyOnWriteArrayList<Entry<String, Long>>();

	public Notifications() {
		super(new String[] { "Notifications" }, "", Type.RENDER, -1, -1);
	}

	public void onEvent(Event e) {
		if (e instanceof EventPreMotion) {
			for (Entry<String, Long> notification : notifications) {
				if (TimeUtil.hasDelayRun(notification.getValue(), delay)) {
					notifications.remove(notification);
				}
			}
		} else if (e instanceof EventPostHudRender) {
			EventPostHudRender event = (EventPostHudRender) e;

			Collections.reverse(notifications);

			int count = 0;
			for (Entry<String, Long> notification : notifications) {
				GuiUtil.drawBorderedRect(event.getScreenWidth() - getFont().getStringWidth(notification.getKey()) - 4, event.getScreenHeight() - 52 + count + 10, event.getScreenWidth() + 2, event.getScreenHeight() - 54 + count, 0.3F, 0xFF000000, 0x55000000);
				GuiUtil.drawStringWithShadow(notification.getKey(), event.getScreenWidth() - getFont().getStringWidth(notification.getKey()) - 2, event.getScreenHeight() - 52 + count, 0xFFFFFFFF, 0.7F);
				count -= 14;
			}

			Collections.reverse(notifications);
		}
	}

	public int delay = 5000;

	private String DELAY = "Delay";
	private List<Value> values = Arrays.asList(new Value[] { new Value(DELAY, 1, 10000, 500), });

	@Override
	public List<Value> getValues() {
		return values;
	}

	@Override
	public Object getValue(String n) {
		if (n.equals(DELAY)) return delay;
		return null;
	}

	@Override
	public void setValue(String n, Object v) {
		if (n.equals(DELAY)) delay = (Integer) v;
	}
}
