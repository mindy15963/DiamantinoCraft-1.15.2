package com.diamantino.diamantinocraft.util.generator;

import com.google.gson.JsonObject;

@Deprecated
@FunctionalInterface
public interface IRecipeBuilder {
    JsonObject build();
}
