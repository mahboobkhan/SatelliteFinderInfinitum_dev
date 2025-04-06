package com.example.satellitefinder.db;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.android.billingclient.api.ProductDetails;
import com.example.satellitefinder.subscription.SkuDetailsModel;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class SubscriptionDao_Impl implements SubscriptionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SkuDetailsModel> __insertionAdapterOfSkuDetailsModel;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  private final SharedSQLiteStatement __preparedStmtOfUpdate;

  public SubscriptionDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSkuDetailsModel = new EntityInsertionAdapter<SkuDetailsModel>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `SkuDetailsModel` (`canPurchase`,`sku`,`type`,`price`,`title`,`description`,`originalJson`,`introductoryPrice`,`freeTrialPeriod`,`priceCurrencyCode`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SkuDetailsModel value) {
        final int _tmp = value.getCanPurchase() ? 1 : 0;
        stmt.bindLong(1, _tmp);
        if (value.getSku() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getSku());
        }
        if (value.getType() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getType());
        }
        if (value.getPrice() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getPrice());
        }
        if (value.getTitle() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getTitle());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getDescription());
        }
        if (value.getOriginalJson() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getOriginalJson());
        }
        if (value.getIntroductoryPrice() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getIntroductoryPrice());
        }
        if (value.getFreeTrialPeriod() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getFreeTrialPeriod());
        }
        if (value.getPriceCurrencyCode() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getPriceCurrencyCode());
        }
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM SkuDetailsModel";
        return _query;
      }
    };
    this.__preparedStmtOfUpdate = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE SkuDetailsModel SET canPurchase = ? WHERE sku = ?";
        return _query;
      }
    };
  }

  @Override
  public void insert(final SkuDetailsModel skuDetailsModel) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfSkuDetailsModel.insert(skuDetailsModel);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public ProductDetails insertOrUpdate(final ProductDetails skuDetails) {
    __db.beginTransaction();
    try {
      ProductDetails _result = SubscriptionDao.DefaultImpls.insertOrUpdate(SubscriptionDao_Impl.this, skuDetails);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertOrUpdate(final String sku, final boolean canPurchase) {
    __db.beginTransaction();
    try {
      SubscriptionDao.DefaultImpls.insertOrUpdate(SubscriptionDao_Impl.this, sku, canPurchase);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public void update(final String sku, final boolean canPurchase) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdate.acquire();
    int _argIndex = 1;
    final int _tmp = canPurchase ? 1 : 0;
    _stmt.bindLong(_argIndex, _tmp);
    _argIndex = 2;
    if (sku == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, sku);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdate.release(_stmt);
    }
  }

  @Override
  public LiveData<List<SkuDetailsModel>> getSubscriptionSkuDetails() {
    final String _sql = "SELECT * FROM SkuDetailsModel WHERE type = 'subs'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"SkuDetailsModel"}, false, new Callable<List<SkuDetailsModel>>() {
      @Override
      public List<SkuDetailsModel> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCanPurchase = CursorUtil.getColumnIndexOrThrow(_cursor, "canPurchase");
          final int _cursorIndexOfSku = CursorUtil.getColumnIndexOrThrow(_cursor, "sku");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfOriginalJson = CursorUtil.getColumnIndexOrThrow(_cursor, "originalJson");
          final int _cursorIndexOfIntroductoryPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "introductoryPrice");
          final int _cursorIndexOfFreeTrialPeriod = CursorUtil.getColumnIndexOrThrow(_cursor, "freeTrialPeriod");
          final int _cursorIndexOfPriceCurrencyCode = CursorUtil.getColumnIndexOrThrow(_cursor, "priceCurrencyCode");
          final List<SkuDetailsModel> _result = new ArrayList<SkuDetailsModel>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final SkuDetailsModel _item;
            final boolean _tmpCanPurchase;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfCanPurchase);
            _tmpCanPurchase = _tmp != 0;
            final String _tmpSku;
            if (_cursor.isNull(_cursorIndexOfSku)) {
              _tmpSku = null;
            } else {
              _tmpSku = _cursor.getString(_cursorIndexOfSku);
            }
            final String _tmpType;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmpType = null;
            } else {
              _tmpType = _cursor.getString(_cursorIndexOfType);
            }
            final String _tmpPrice;
            if (_cursor.isNull(_cursorIndexOfPrice)) {
              _tmpPrice = null;
            } else {
              _tmpPrice = _cursor.getString(_cursorIndexOfPrice);
            }
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpOriginalJson;
            if (_cursor.isNull(_cursorIndexOfOriginalJson)) {
              _tmpOriginalJson = null;
            } else {
              _tmpOriginalJson = _cursor.getString(_cursorIndexOfOriginalJson);
            }
            final String _tmpIntroductoryPrice;
            if (_cursor.isNull(_cursorIndexOfIntroductoryPrice)) {
              _tmpIntroductoryPrice = null;
            } else {
              _tmpIntroductoryPrice = _cursor.getString(_cursorIndexOfIntroductoryPrice);
            }
            final String _tmpFreeTrialPeriod;
            if (_cursor.isNull(_cursorIndexOfFreeTrialPeriod)) {
              _tmpFreeTrialPeriod = null;
            } else {
              _tmpFreeTrialPeriod = _cursor.getString(_cursorIndexOfFreeTrialPeriod);
            }
            final String _tmpPriceCurrencyCode;
            if (_cursor.isNull(_cursorIndexOfPriceCurrencyCode)) {
              _tmpPriceCurrencyCode = null;
            } else {
              _tmpPriceCurrencyCode = _cursor.getString(_cursorIndexOfPriceCurrencyCode);
            }
            _item = new SkuDetailsModel(_tmpCanPurchase,_tmpSku,_tmpType,_tmpPrice,_tmpTitle,_tmpDescription,_tmpOriginalJson,_tmpIntroductoryPrice,_tmpFreeTrialPeriod,_tmpPriceCurrencyCode);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<SkuDetailsModel>> getInappSkuDetails() {
    final String _sql = "SELECT * FROM SkuDetailsModel WHERE type = 'inapp'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"SkuDetailsModel"}, false, new Callable<List<SkuDetailsModel>>() {
      @Override
      public List<SkuDetailsModel> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCanPurchase = CursorUtil.getColumnIndexOrThrow(_cursor, "canPurchase");
          final int _cursorIndexOfSku = CursorUtil.getColumnIndexOrThrow(_cursor, "sku");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfOriginalJson = CursorUtil.getColumnIndexOrThrow(_cursor, "originalJson");
          final int _cursorIndexOfIntroductoryPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "introductoryPrice");
          final int _cursorIndexOfFreeTrialPeriod = CursorUtil.getColumnIndexOrThrow(_cursor, "freeTrialPeriod");
          final int _cursorIndexOfPriceCurrencyCode = CursorUtil.getColumnIndexOrThrow(_cursor, "priceCurrencyCode");
          final List<SkuDetailsModel> _result = new ArrayList<SkuDetailsModel>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final SkuDetailsModel _item;
            final boolean _tmpCanPurchase;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfCanPurchase);
            _tmpCanPurchase = _tmp != 0;
            final String _tmpSku;
            if (_cursor.isNull(_cursorIndexOfSku)) {
              _tmpSku = null;
            } else {
              _tmpSku = _cursor.getString(_cursorIndexOfSku);
            }
            final String _tmpType;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmpType = null;
            } else {
              _tmpType = _cursor.getString(_cursorIndexOfType);
            }
            final String _tmpPrice;
            if (_cursor.isNull(_cursorIndexOfPrice)) {
              _tmpPrice = null;
            } else {
              _tmpPrice = _cursor.getString(_cursorIndexOfPrice);
            }
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpOriginalJson;
            if (_cursor.isNull(_cursorIndexOfOriginalJson)) {
              _tmpOriginalJson = null;
            } else {
              _tmpOriginalJson = _cursor.getString(_cursorIndexOfOriginalJson);
            }
            final String _tmpIntroductoryPrice;
            if (_cursor.isNull(_cursorIndexOfIntroductoryPrice)) {
              _tmpIntroductoryPrice = null;
            } else {
              _tmpIntroductoryPrice = _cursor.getString(_cursorIndexOfIntroductoryPrice);
            }
            final String _tmpFreeTrialPeriod;
            if (_cursor.isNull(_cursorIndexOfFreeTrialPeriod)) {
              _tmpFreeTrialPeriod = null;
            } else {
              _tmpFreeTrialPeriod = _cursor.getString(_cursorIndexOfFreeTrialPeriod);
            }
            final String _tmpPriceCurrencyCode;
            if (_cursor.isNull(_cursorIndexOfPriceCurrencyCode)) {
              _tmpPriceCurrencyCode = null;
            } else {
              _tmpPriceCurrencyCode = _cursor.getString(_cursorIndexOfPriceCurrencyCode);
            }
            _item = new SkuDetailsModel(_tmpCanPurchase,_tmpSku,_tmpType,_tmpPrice,_tmpTitle,_tmpDescription,_tmpOriginalJson,_tmpIntroductoryPrice,_tmpFreeTrialPeriod,_tmpPriceCurrencyCode);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public SkuDetailsModel getById(final String sku) {
    final String _sql = "SELECT * FROM SkuDetailsModel WHERE sku = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (sku == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, sku);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfCanPurchase = CursorUtil.getColumnIndexOrThrow(_cursor, "canPurchase");
      final int _cursorIndexOfSku = CursorUtil.getColumnIndexOrThrow(_cursor, "sku");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfOriginalJson = CursorUtil.getColumnIndexOrThrow(_cursor, "originalJson");
      final int _cursorIndexOfIntroductoryPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "introductoryPrice");
      final int _cursorIndexOfFreeTrialPeriod = CursorUtil.getColumnIndexOrThrow(_cursor, "freeTrialPeriod");
      final int _cursorIndexOfPriceCurrencyCode = CursorUtil.getColumnIndexOrThrow(_cursor, "priceCurrencyCode");
      final SkuDetailsModel _result;
      if(_cursor.moveToFirst()) {
        final boolean _tmpCanPurchase;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfCanPurchase);
        _tmpCanPurchase = _tmp != 0;
        final String _tmpSku;
        if (_cursor.isNull(_cursorIndexOfSku)) {
          _tmpSku = null;
        } else {
          _tmpSku = _cursor.getString(_cursorIndexOfSku);
        }
        final String _tmpType;
        if (_cursor.isNull(_cursorIndexOfType)) {
          _tmpType = null;
        } else {
          _tmpType = _cursor.getString(_cursorIndexOfType);
        }
        final String _tmpPrice;
        if (_cursor.isNull(_cursorIndexOfPrice)) {
          _tmpPrice = null;
        } else {
          _tmpPrice = _cursor.getString(_cursorIndexOfPrice);
        }
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        final String _tmpDescription;
        if (_cursor.isNull(_cursorIndexOfDescription)) {
          _tmpDescription = null;
        } else {
          _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        }
        final String _tmpOriginalJson;
        if (_cursor.isNull(_cursorIndexOfOriginalJson)) {
          _tmpOriginalJson = null;
        } else {
          _tmpOriginalJson = _cursor.getString(_cursorIndexOfOriginalJson);
        }
        final String _tmpIntroductoryPrice;
        if (_cursor.isNull(_cursorIndexOfIntroductoryPrice)) {
          _tmpIntroductoryPrice = null;
        } else {
          _tmpIntroductoryPrice = _cursor.getString(_cursorIndexOfIntroductoryPrice);
        }
        final String _tmpFreeTrialPeriod;
        if (_cursor.isNull(_cursorIndexOfFreeTrialPeriod)) {
          _tmpFreeTrialPeriod = null;
        } else {
          _tmpFreeTrialPeriod = _cursor.getString(_cursorIndexOfFreeTrialPeriod);
        }
        final String _tmpPriceCurrencyCode;
        if (_cursor.isNull(_cursorIndexOfPriceCurrencyCode)) {
          _tmpPriceCurrencyCode = null;
        } else {
          _tmpPriceCurrencyCode = _cursor.getString(_cursorIndexOfPriceCurrencyCode);
        }
        _result = new SkuDetailsModel(_tmpCanPurchase,_tmpSku,_tmpType,_tmpPrice,_tmpTitle,_tmpDescription,_tmpOriginalJson,_tmpIntroductoryPrice,_tmpFreeTrialPeriod,_tmpPriceCurrencyCode);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
