package oop.project.foodjar.Database.Local;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.RxRoom;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import io.reactivex.Flowable;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import oop.project.foodjar.Database.Model.Cart;

@SuppressWarnings({"unchecked", "deprecation"})
public final class CartDAO_Impl implements CartDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Cart> __insertionAdapterOfCart;

  private final EntityDeletionOrUpdateAdapter<Cart> __deletionAdapterOfCart;

  private final EntityDeletionOrUpdateAdapter<Cart> __updateAdapterOfCart;

  private final SharedSQLiteStatement __preparedStmtOfEmptyCart;

  public CartDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCart = new EntityInsertionAdapter<Cart>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `Cart` (`id`,`img`,`name`,`amount`,`price`,`size`,`restName`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Cart value) {
        stmt.bindLong(1, value.id);
        stmt.bindLong(2, value.img);
        if (value.name == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.name);
        }
        stmt.bindLong(4, value.amount);
        stmt.bindDouble(5, value.price);
        if (value.size == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.size);
        }
        if (value.restName == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.restName);
        }
      }
    };
    this.__deletionAdapterOfCart = new EntityDeletionOrUpdateAdapter<Cart>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Cart` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Cart value) {
        stmt.bindLong(1, value.id);
      }
    };
    this.__updateAdapterOfCart = new EntityDeletionOrUpdateAdapter<Cart>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `Cart` SET `id` = ?,`img` = ?,`name` = ?,`amount` = ?,`price` = ?,`size` = ?,`restName` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Cart value) {
        stmt.bindLong(1, value.id);
        stmt.bindLong(2, value.img);
        if (value.name == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.name);
        }
        stmt.bindLong(4, value.amount);
        stmt.bindDouble(5, value.price);
        if (value.size == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.size);
        }
        if (value.restName == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.restName);
        }
        stmt.bindLong(8, value.id);
      }
    };
    this.__preparedStmtOfEmptyCart = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM Cart";
        return _query;
      }
    };
  }

  @Override
  public void insertToCart(final Cart... carts) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfCart.insert(carts);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteCartItem(final Cart cart) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfCart.handle(cart);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void UpdateCart(final Cart... carts) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfCart.handleMultiple(carts);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void emptyCart() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfEmptyCart.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfEmptyCart.release(_stmt);
    }
  }

  @Override
  public Flowable<List<Cart>> getCartItem() {
    final String _sql = "SELECT* FROM Cart";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return RxRoom.createFlowable(__db, false, new String[]{"Cart"}, new Callable<List<Cart>>() {
      @Override
      public List<Cart> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfImg = CursorUtil.getColumnIndexOrThrow(_cursor, "img");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
          final int _cursorIndexOfSize = CursorUtil.getColumnIndexOrThrow(_cursor, "size");
          final int _cursorIndexOfRestName = CursorUtil.getColumnIndexOrThrow(_cursor, "restName");
          final List<Cart> _result = new ArrayList<Cart>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Cart _item;
            _item = new Cart();
            _item.id = _cursor.getInt(_cursorIndexOfId);
            _item.img = _cursor.getInt(_cursorIndexOfImg);
            _item.name = _cursor.getString(_cursorIndexOfName);
            _item.amount = _cursor.getInt(_cursorIndexOfAmount);
            _item.price = _cursor.getDouble(_cursorIndexOfPrice);
            _item.size = _cursor.getString(_cursorIndexOfSize);
            _item.restName = _cursor.getString(_cursorIndexOfRestName);
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
  public Flowable<List<Cart>> getCartItemById(final int cartItemId) {
    final String _sql = "SELECT* FROM Cart WHERE id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, cartItemId);
    return RxRoom.createFlowable(__db, false, new String[]{"Cart"}, new Callable<List<Cart>>() {
      @Override
      public List<Cart> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfImg = CursorUtil.getColumnIndexOrThrow(_cursor, "img");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
          final int _cursorIndexOfSize = CursorUtil.getColumnIndexOrThrow(_cursor, "size");
          final int _cursorIndexOfRestName = CursorUtil.getColumnIndexOrThrow(_cursor, "restName");
          final List<Cart> _result = new ArrayList<Cart>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Cart _item;
            _item = new Cart();
            _item.id = _cursor.getInt(_cursorIndexOfId);
            _item.img = _cursor.getInt(_cursorIndexOfImg);
            _item.name = _cursor.getString(_cursorIndexOfName);
            _item.amount = _cursor.getInt(_cursorIndexOfAmount);
            _item.price = _cursor.getDouble(_cursorIndexOfPrice);
            _item.size = _cursor.getString(_cursorIndexOfSize);
            _item.restName = _cursor.getString(_cursorIndexOfRestName);
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
  public int countCartItem() {
    final String _sql = "SELECT COUNT(*) from Cart";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if(_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public float sumPrice() {
    final String _sql = "SELECT SUM(price) AS value from Cart";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final float _result;
      if(_cursor.moveToFirst()) {
        _result = _cursor.getFloat(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
