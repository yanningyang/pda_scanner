import 'package:flutter/material.dart';
import 'package:pda_scanner/pda_listener.dart';

class PageBeta extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => PageBetaState();
}

class PageBetaState extends PdaListenerState<PageBeta> {
  var _code;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('PageBeta'),
      ),
      body: Column(
        children: <Widget>[
          Text('Scanning result: $_code\n'),
          RaisedButton(child: Text('Back to Alhpa'), onPressed: () => Navigator.of(context).pop()),
        ],
      ),
    );
  }

  @override
  void onEvent(Object event) {
    setState(() {
      _code = event;
      print("ChannelPage: $event");
    });
  }
}
