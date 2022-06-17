# Weather Flow Smart Weather Binding

This is an OpenHAB 2 binding for the Weather Flow Smart Weather station and sensors. The binding uses the UDP broadcast protocol provided by the Smart Weather Hub. Just connect the hub to the same network as your OpenHAB server and the binding will take care of the rest.

## Supported Things

Currently, this binding supports the Smart Weather Hub, Sky, Air and Tempest sensors. Additionally, this binding provides access to Weatherflow Better Forecast data, which is then enriched with some additional fields to ease construction of custom widgets in the OpenHAB UI. 

## Discovery

Supported devices connected to the same network as OpenHAB are automatically detected and should appear in your OpenHAB inbox. 

The binding supports multiple sensors of each type, so if, for example, you have more than one Air sensor, you should see them detected automatically.

## Binding Configuration

No configuration required for SmartWeather hardware! Just place your Hub on the same network as OpenHAB and it will be discovered automatically.

To use the Better Forecast thing, you'll need your Station ID (available in your personal station URL after logging into the weatherflow website [1]) as well as an authorization token (which can be generated from the smartweather API [2])

## Channels

_To be completed_

## Contributing

Snapshot binaries are available form the download section at:

https://bitbucket.org/hww3/org.openhab.binding.weatherflowsmartweather/downloads/

See the master git repository at: 

https://git.sr.ht/~hww3/org.openhab.binding.weatherflowsmartweather

You can use git send-email to submit patches to the mailing list: 

https://lists.sr.ht/~hww3/weatherflowsmartweather

## TODO

- Support Strike (and Fast Wind/Rain Start) events Done
- Add derived datapoints (feels like, dewpoint, etc) Done, see weathercalulations binding
- Add access to Better Forecast data Done

[1] https://tempestwx.com
[2] https://tempestwx.com/settings/tokens
