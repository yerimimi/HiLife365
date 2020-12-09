package com.hilifecare.util.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class GsonModule {

    @Provides
    @Singleton
    public GsonBuilder provideDefaultGsonBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Money.class, new MoneyTypeConverter());
        gsonBuilder.registerTypeAdapter(CurrencyUnit.class, new CurrencyUnitTypeConverter()); 
        
        gsonBuilder.registerTypeAdapterFactory(new AutoValueTypeAdapterFactory());

        return gsonBuilder;
    }

    @Provides
    @Singleton
    Gson provideGson(GsonBuilder gsonBuilder) {
        return gsonBuilder.create();
    }

}
