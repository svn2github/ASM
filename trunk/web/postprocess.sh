#!/bin/sh

cd doc
sed -e '/TOBEREPLACED/r tutorial-body.html' -e '/TOBEREPLACED/d' tutorial-holder.html > tutorial.html
sed -e '/TOBEREPLACED/r tutorial-body.html' -e '/TOBEREPLACED/d' tutorial-holder_print.html > tutorial_print.html
