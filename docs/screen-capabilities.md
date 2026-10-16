# Screen capabilities

What screen reading can do, app by app. Filled in by WS-17/WS-18 as each app is tested. Tested on one phone model with OS and app auto-updates frozen before the demo.

| App | Package | Read | Scroll | Back | Tap label | Screenshot rung | Summary | Notes |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Mock messaging app | (tests/mock_screens) | | | | | | | Fixture-based |
| WhatsApp | com.whatsapp | | | | | | | Read-only; never send |
| Facebook | com.facebook.katana | | | | | | | Post summary |
| Third app (TD-007) | | | | | | | | |

**Always blocked:** password fields, `FLAG_SECURE` windows, deny-listed packages (banking, wallets). Nothing from a blocked screen is read or sent.

**Never performed in the MVP:** send, delete, buy, pay, post, confirm.
