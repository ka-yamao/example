# ReactNative react-native-config を使ってみる

## 環境

- macOS Mojave 10.14.4
- サンプルプロジェクト名：example

## プロジェクトを作成

```
react-native init example

```

## インストール、設定

```
# react-native-configを追加
$ yarn add react-native-config

# react-native-configをreact-nativeのプロジェクトに適用する
$ react-native link react-native-config

```

## Android

`/android/app/build.gradle`に以下を追加

https://github.com/ka-yamao/example/blob/94e23d084cd16996f6f63599f00696d9be23d519/android/app/build.gradle#L2

## .env ファイルを作成

### 開発版の.env ファイル作成

```
example/.env

ENV=dev
WEB_URL=https://dev.example.com/
```

- 開発はサブドメインに`dev`がついている

### 製品版の.env ファイル作成

```
example/.env.prod

ENV=dev
WEB_URL=https://example.com/
```

- ファイル名は、`.env.prod`にして製品はサブドメインなし

## 確認するため App.js を修正

### react-native-config を import する

```
example/App.js

// react-native-configをインポート
import Config from "react-native-config";

```

### .env の環境変数を使ってみる

```
export default class App extends Component<Props> {
  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>Welcome to React Native!</Text>
        <Text style={styles.instructions}>To get started, edit App.js</Text>
        <Text style={styles.instructions}>{instructions}</Text>

        // WEB_URLをテキストで表示してみる
        <Text style={styles.instructions}>{Config.WEB_URL}</Text>
      </View>
    );
  }
}
```

## 実機で確認

###

```
react-native run-ios
```

```
ENVFILE=.env.prod react-native run-ios
```

## 参考サイト

https://github.com/luggit/react-native-config
https://blog.mitsuruog.info/2019/03/react-native-config
