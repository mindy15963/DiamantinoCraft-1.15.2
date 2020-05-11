/*
 * SilentLib - ModifierHelper
 * Copyright (C) 2018 SilentChaos512
 *
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.diamantino.diamantinocraft.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import com.diamantino.diamantinocraft.utils.MathUtils;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Helper methods for working with entity attributes.
 *
 * @author SilentChaos512
 * @since 2.3.2
 */
public final class AttributeHelper {
    private AttributeHelper() {throw new IllegalAccessError("Utility class");}

    public static void apply(LivingEntity entity, IAttribute attribute, AttributeModifier modifier) {
        IAttributeInstance instance = entity.getAttribute(attribute);
        apply(instance, modifier);
    }

    public static void apply(@Nullable IAttributeInstance attributeInstance, AttributeModifier modifier) {
        if (attributeInstance == null) return;
        AttributeModifier currentMod = attributeInstance.getModifier(modifier.getID());

        if (currentMod != null && (!MathUtils.doublesEqual(currentMod.getAmount(), modifier.getAmount()) || currentMod.getOperation() != modifier.getOperation())) {
            // Modifier changed, so it needs to be reapplied
            attributeInstance.removeModifier(currentMod);
        } else {
            attributeInstance.applyModifier(modifier);
        }
    }

    public static void remove(LivingEntity entity, IAttribute attribute, UUID uuid) {
        IAttributeInstance instance = entity.getAttribute(attribute);
        remove(instance, uuid);
    }

    public static void remove(@Nullable IAttributeInstance attributeInstance, UUID uuid) {
        if (attributeInstance == null) return;
        attributeInstance.removeModifier(uuid);
    }
}
