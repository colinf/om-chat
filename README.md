# Flux Chat Example in Om Next

This is an [Om Next](https://github.com/omcljs/om/wiki/Quick-Start-%28om.next%29) re-creation of the [flux-chat](https://github.com/facebook/flux/tree/master/examples/flux-chat) project provided by Facebook as an example of [Flux](http://facebook.github.io/flux/), their architecture for building user interfaces with React.

## Om Next Status

Om Next is a forthcoming new version of [Om](https://github.com/omcljs/om), a ClojureScript interface to React. It is currently under active development and is therefore changing rapidly.

*This project is based on Om Next as per release 1.0.0-alpha22 of the Om project. Please note that it is distinctly possible that the APIs for Om Next will change in subsequent versions.*

## Setup

Clone this project and run the following command from its root directory:

    lein run -m clojure.main script/figwheel.clj

Then open your browser at [localhost:3449](http://localhost:3449/).

## Project Status ##

The project fully re-creates the UI of the flux-chat example. The only known difference is that when a new message is posted to a thread, the text and time of the last message are updated in the thread list on the left side of the screen. In flux-chat they are not updated (it is only an example!) but I found that this functionality was pretty much automatically provided due to the global application state of Om Next.

One of the purposes of flux-chat is to show how flux actions can call a Web API to retrieve/update data. This interaction with a server API is not yet represented in my Om Next project, but hopefully I will add this in due course.

## License

See the file LICENSE.txt in the root directory of this project.
