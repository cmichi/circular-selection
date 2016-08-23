# Circular Selection

This repository contains a circular list selection interface for Android
Wear. The interface is optimized for round smartwatches. The app supports 
multiple, different interaction techniques for the list selection.

I developed these conceptual ideas and the app in a small project which I did as 
part of my Master studies in the Media Informatics program at the University Ulm.
As a next step, I conducted an academic study to evaluate different interaction
techniques and interface variations against each other. In the study the usability 
of the interface has been evaluatedin terms of speed, error rate, hedonistic qualities
(i.e. user satisfaction), etc..
The results have in the meantime been published as an academic paper: 
[Circular Selection: Optimizing List Selection for Smartwatches]().

The basic interaction is that the ring on the bezel of the smartwatch can
be used to select a sublist of a larger list.

I have only tested the software with the Motorola Moto 360 and am unsure
how it behaves on other Smartwatches.


## How it looks

![Circular Selection](https://github.com/cmichi/circular-selection/raw/master/images/circular-selection.png)

The black area (the ``flat tire'') is a hardware characteristic of the Moto 360,
behind this black area sensors are placed. The software itself is
independent of this shortcoming.


## Interface Variations

The app supports variations in term of 

 * Direction of the characters on the ring (A-Z or Z-A)
 * Interaction technique (How a character is selected): Single Tap, Rotation, etc.
 * Button Size: the buttons for individual characters on the ring can be
   either of equal size for all buttons or the button size can be different
   for each character, based on the size of the sublist behind it (i.e.
   the number of items which start with this certain character, meaning
   they have this character as a first character).

The paper describes the influence of all these variables on the usability of the 
interface in further detail.

## Citations (BibTeX/BibLaTeX)

	@inproceedings{iwsc2016,
		title = {Circular Selection: Optimizing List Selection for Smartwatches},
		booktitle={Proceedings of the 2015 ACM International Symposium on Wearable Computers},
		year={2016},
		organization={ACM},
		author = {Plaumann, Katrin and Müller, Michael and Rukzio, Enrico},
		langid = {english},
		langidopts = {variant=british},
	}

	ToDo: add pdf and acm link!

## License

The code is licensed under the MIT license.

	Copyright (c) 2015

		Michael Mueller <http://micha.elmueller.net/>

	Permission is hereby granted, free of charge, to any person obtaining
	a copy of this software and associated documentation files (the
	"Software"), to deal in the Software without restriction, including
	without limitation the rights to use, copy, modify, merge, publish,
	distribute, sublicense, and/or sell copies of the Software, and to
	permit persons to whom the Software is furnished to do so, subject to
	the following conditions:

	The above copyright notice and this permission notice shall be
	included in all copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
	EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
	MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
	NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
	LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
	OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
	WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
