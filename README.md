# Weather Flow Smart Weather Binding

This is an OpenHAB 2 binding for the Weather Flow Smart Weather station and sensors. The binding uses the UDP broadcast protocol provided by the Smart Weathe Hub. Just connect the hub to the same network as your OpenHAB server and the binding will take care of the rest.

## Supported Things

Currently, this binding supports the Smart Weather Hub and the Smart Weather Air sensor. 

## Discovery

Supported devices connected to the same network as OpenHAB are automatically detected and should appear in your OpenHAB inbox. 

The binding supports multiple sensors of each type, so if, for example, you have more than one Air sensor, you should see them detected automatically.

## Binding Configuration

No configuration required!

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
