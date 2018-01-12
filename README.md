Master: [![Build Status](https://travis-ci.org/RossBlassingame/JPL-CUSeniorProjects.svg?branch=master)](https://travis-ci.org/RossBlassingame/JPL-CUSeniorProjects)
Dev: [![Build Status](https://travis-ci.org/RossBlassingame/JPL-CUSeniorProjects.svg?branch=dev)](https://travis-ci.org/RossBlassingame/JPL-CUSeniorProjects)

# CU Senior Projects -- JPL Team

## M.A.R.S.
The Martian Autonomous Routing System (or MARS) is a command-line utility that produces lists of coordinates describing possible routes for a rover to take to reach a goal coordinate from the start, whilst navigating a provided elevation map, within the bounds of its physical capabilities.

## Members:
 - Ross Blassingame
 - Robert Ballard
 - Shawn Polson
 - Josh Jenkins
 - Chandler Samuels
 - Dean Moser
 
## Sponsor
This project is sponsored by NASA Jet Propulsion Laboratory, with Marcel Llopis providing advice and oversight for the work.

## Usage

### Input

To use the program, you must provide it:
- a compatible elevation map (see below)
- a start coordinate (in pixels) corresponding to a location on the input map
- an end coordinate (in pixels) also corresponding to the map
- a specified algorithm to run (see below)
- specifications for a theoretical rover, including a maximum slope it can safely traverse, as well as field of view for using the limited route algorithm

Compatible elevation map formats include:

| Format    | Accepted Extension(s) |
| --------- | --------------------- |
| GeoTIFF   | .tiff, .tif           |
 
Available algorithms include:

| Algorithm       | Prompt Key | Description                                                                               |
| --------------- | ---------- | ----------------------------------------------------------------------------------------- |
| Unlimited scope | U          | Navigates from start to goal with perfect information with regards to the elevation map.  |
| Limited scope   | L          | Navigates from start to goal with information gathered as it traverses the elevation map. |
 
This information can be provided to the utility by simply running it, and following the prompts.
 
### Execution

Once the program completes, the resultant path the chosen algorithm determined will be output to the terminal in a human-readable format. It can be run again to check against another algorithm or saved to a CSV.

## Copyright

something open source TODO

