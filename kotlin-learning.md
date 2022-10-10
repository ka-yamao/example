---
marp: true
---

# Kotlin サンプルアプリで勉強したこと共有します。

- apply / also / let / run / with
- Navigation Component

---

# apply / also / let / run / with とは

- Kotlin のスコープ関数です。
- インスタンスに対して、連続して処理をするような場合、スコープ関数を使えばコードを簡潔に書くことが可能になります。

---

# 用途で２つに分けると

- プロパティを設定する apply, also と、メソッドを実行する let, run, with です。

---

# こんなクラスがあっっとします。

```
class Player(val name: String){
    var age = 0
    var team: String? = null
}
```

このクラスのインスタンスを生成してプロパティを設定するには、

```
val player = Player("大谷翔平")
player.age = 28
player.team = "エンゼルス"

print(player.team)
```

このようにまずクラスのインスタンスを生成し、そのインスタンスの各プロパティを設定していくというのが通常の手順です。
このあと、Player class を使って説明します。

---

# apply

```
val player = Player("大谷翔平").apply{
  age = 28
  team = "エンゼルス"
}
print(player.team) // => エンゼルス
```

インスタンス生成と同時に apply を呼び出し、その後にラムダ式でプロパティを設定しています。
戻り値はプロパティが設定されたレシーバー（Player インスタンス）になります。

- apply は呼び出し元を「this」で参照し、this は省略可能
- apply の戻り値は呼び出し元のインスタンス

---

# also

```
val player = Player("大谷翔平").also{
    it.age = 28
    it.team = "エンゼルス"
}
print(player.team) // => エンゼルス
```

also の中のラムダ式で、Player インスタンスは「it」で参照されています。こちらは省略不可です。

この it はラムダ式の引数が 1 つだけのときに使える it であり、つまり also は呼び出し元を、ラムダの引数として扱っているということになります。

- also は呼び出し元を「it」で参照し、省略不可
- 戻り値は呼び出し元のインスタンス

```
apply の場合はラムダの中で(省略可能ですが)thisを使うため、クラスメソッドなどの内部処理で使用される場合、thisが指し示すものがラムダ内だけ変わってしまうという場合もあります。
その点alsoは呼び出し元の参照用としてのthisを使用しないため、ラムダの内も外もthisの意味合いが変わらないという利点があります。
```

---

# let

```
val str = "hoge".let {
  print(it) // => hoge
  it.toUpperCase()
}
print(str) // => HOGE
```

let は apply や also とはまた違った利用方法で、プロパティを設定するのではなく、そのレシーバー（ここでは String クラス）に対してメソッドを実行するといった使い方が主になってきます。

- let は呼び出し元を it で参照し、省略不可
- let の戻り値はラムダの結果

---

## let で null 許容型を扱う

```
val player = Player("大谷翔平")
player.team?.let{
  print(it)
} ?:print("戦力外")

// => 戦力外
```

- it を使います。
- Kotlin には三項演算子がないです。if 文だったり、エルビス演算子を使って null だったら、null じゃなかったらを実装する。
- https://ticktakclock.hatenablog.com/entry/2020/02/22/203408

---

# run

```
val player = Player("大谷翔平")
player.team = "エンゼルス"
val team = player.team?.run{
  "チームは、$this"
} ?: "チームは、未設定"

print(team) // => チームは、エンゼルス
```

働きとしては let と同じですが apply と also の関係と同じように、run を使用する場合、ラムダの外側で「this」を使用していないかどうかチェックし、もし使用しているのであれば、そこは let を使うべき場面となるかもしれません。

- run は呼び出し元を this で参照し、省略可
- run の戻り値はラムダの結果

---

# with

```
val str = with("hoge"){
  this.toUpperCase()
}
print(str) // => HOGE

val p = with(Player("大谷翔平")){
  this.name
}
print(p)  // => 大谷翔平
```

with はこれまでのスコープ関数と記述方法が異なるため、若干異質の存在です。ただ働きとしては run と同じです。そのため、with はあまり見かけることは無いかもしれません。

- with は呼び出し元を this で参照し、省略可
- with の戻り値はラムダの結果

---

# Android アプリでの利用例をいくつか

### apply, run, also

- https://github.com/ka-yamao/umpire/blob/3cdf3b6b27fc7193d507151d67417d4d855d22bd/android/app/src/main/java/com/c/local/umpire/fragment/GamesFragment.kt#L52-L86

---

# まとめ

| 関数  | 呼び出し元の参照 (this は省略可能) | 戻り値                   |
| ----- | ---------------------------------- | ------------------------ |
| apply | this                               | 呼び出し元のインスタンス |
| also  | it                                 | 呼び出し元のインスタンス |
| let   | it                                 | ラムダの結果             |
| run   | this                               | ラムダの結果             |
| with  | this                               | ラムダの結果             |

---

## 参考サイト

- https://qiita.com/ngsw_taro/items/d29e3080d9fc8a38691e
- https://qiita.com/JohnSmithWithHaruhi/items/e8f411c379483d4902aa
- https://pouhon.net/kotlin-sfunction/1392/

---
