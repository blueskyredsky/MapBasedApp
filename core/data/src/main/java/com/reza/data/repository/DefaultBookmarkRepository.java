package com.reza.data.repository;

import com.google.android.libraries.places.api.model.PlaceTypes;
import com.reza.data.R;
import com.reza.database.BookmarkDao;
import com.reza.database.BookmarkEntity;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class DefaultBookmarkRepository implements BookmarkRepository {

    private final BookmarkDao bookmarkDao;
    private final String GAS = "GAS";
    private final String LODGING = "LODGING";
    public static final String OTHER = "OTHER";
    private final String RESTAURANT = "RESTAURANT";
    private final String SHOPPING = "SHOPPING";

    @Inject
    public DefaultBookmarkRepository(BookmarkDao bookmarkDao) {
        this.bookmarkDao = bookmarkDao;
    }

    @Override
    public Flowable<List<BookmarkEntity>> getAllBookmarks() {
        return bookmarkDao.getAllBookmarks();
    }

    @Override
    public Single<BookmarkEntity> getBookmark(Long bookmarkId) {
        return bookmarkDao.getBookmark(bookmarkId);
    }

    @Override
    public Single<BookmarkEntity> getBookmark(String placeId) {
        return bookmarkDao.getBookmark(placeId);
    }

    @Override
    public Single<Long> addBookmark(BookmarkEntity bookmark) {
        return bookmarkDao.addBookmark(bookmark);
    }

    @Override
    public Completable updateBookmark(BookmarkEntity bookmark) {
        return bookmarkDao.updateBookmark(bookmark);
    }

    @Override
    public Completable deleteBookmark(BookmarkEntity bookmark) {
        return bookmarkDao.deleteBookmark(bookmark);
    }

    @Override
    public String placeTypeToCategory(String placeType) {
        String category = OTHER;
        if (categoryMap.containsKey(placeType)) {
            category = categoryMap.get(placeType);
        }
        return category;
    }

    @Override
    public Integer getCategoryResourceId(String placeCategory) {
        return allCategories.get(placeCategory);
    }

    private final HashMap<String , String> categoryMap = buildCategoryMap();

    private HashMap<String , String> buildCategoryMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put(PlaceTypes.BAKERY, RESTAURANT);
        map.put(PlaceTypes.BAR, RESTAURANT);
        map.put(PlaceTypes.CAFE, RESTAURANT);
        map.put(PlaceTypes.FOOD, RESTAURANT);
        map.put(PlaceTypes.RESTAURANT, RESTAURANT);
        map.put(PlaceTypes.MEAL_DELIVERY, RESTAURANT);
        map.put(PlaceTypes.MEAL_TAKEAWAY, RESTAURANT);
        map.put(PlaceTypes.GAS_STATION, GAS);
        map.put(PlaceTypes.CLOTHING_STORE, SHOPPING);
        map.put(PlaceTypes.DEPARTMENT_STORE, SHOPPING);
        map.put(PlaceTypes.FURNITURE_STORE, SHOPPING);
        map.put(PlaceTypes.SUPERMARKET, SHOPPING);
        map.put(PlaceTypes.HARDWARE_STORE, SHOPPING);
        map.put(PlaceTypes.HOME_GOODS_STORE, SHOPPING);
        map.put(PlaceTypes.JEWELRY_STORE, SHOPPING);
        map.put(PlaceTypes.SHOE_STORE, SHOPPING);
        map.put(PlaceTypes.SHOPPING_MALL, SHOPPING);
        map.put(PlaceTypes.STORE, SHOPPING);
        map.put(PlaceTypes.LODGING, LODGING);
        map.put(PlaceTypes.ROOM, LODGING);
        return map;
    }

    private final HashMap<String, Integer> allCategories = buildCategories();

    private HashMap<String, Integer> buildCategories() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put(GAS, R.drawable.ic_gas);
        map.put(LODGING, R.drawable.ic_lodging);
        map.put(OTHER, R.drawable.ic_other);
        map.put(RESTAURANT, R.drawable.ic_restaurant);
        map.put(SHOPPING, R.drawable.ic_shopping);
        return map;
    }
}